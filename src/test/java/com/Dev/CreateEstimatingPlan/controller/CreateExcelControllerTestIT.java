package com.Dev.CreateEstimatingPlan.controller;

import com.Dev.CreateEstimatingPlan.exception.ExcelReadHandlingException;
import com.Dev.CreateEstimatingPlan.exception.GroupNotFoundException;
import com.Dev.CreateEstimatingPlan.serviceImpl.ExcelFileCreationService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("testConf")
@WithMockUser(authorities = {"ROLE_USER"})
class CreateExcelControllerTestIT {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ExcelFileCreationService excelFileCreationService;
    @Autowired
    private CreateExcelController controller;
    private static final String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";


    // download template

    @Test
    void downloadTemplateUk_shouldReturnOK() throws Exception {
        String nameFileUk = "ПланВивченняНД_uk.xlsm";
        String encodedFilename = URLEncoder.encode(nameFileUk, StandardCharsets.UTF_8);
        mockMvc.perform(get("/DownloadTemplateUk"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString(encodedFilename)));
    }

    @Test
    void downloadTemplateEn_shouldReturnOK() throws Exception {
        mockMvc.perform(get("/DownloadTemplateEn"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("ADStudyPlan_en.xlsm")));
    }

    // upload
    @Test
    void shouldReturnUploadPageSuccessful() throws Exception {
        mockMvc.perform(get("/UploadFile"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateExcelFile/CreateExcel"));
    }

    @Test
    void shouldThrowRedirectWithErrorWhenFileIsEmpty() throws Exception {
        String error = "Файл не вибрано!";
        MockMultipartFile emptyFile = new MockMultipartFile("file-upl", "", contentType, new byte[0]);

        mockMvc.perform(multipart("/UploadFile")
                        .file(emptyFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/UploadFile"))
                .andExpect(flash().attribute("error", error));
    }

    @Test
    void shouldThrowExcelReadHandlingExceptionWhenHasErrorInSheets() throws Exception {
        String errorMessageFirst = "Структура вивчення";
        String errorMessageSecond = "Не вказано групу";
        String error = "Помилка в аркуші 'Структура вивчення' — Не вказано групу";

        MockMultipartFile file = new MockMultipartFile(
                "file-upl",
                "test.xlsx",
                "application/vnd.ms-excel",
                "test".getBytes());

        when(excelFileCreationService.createExcelTable(any()))
                .thenThrow(new ExcelReadHandlingException(errorMessageFirst, errorMessageSecond));

        MvcResult result = mockMvc.perform(multipart("/UploadFile")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/UploadFile"))
                .andExpect(flash().attributeExists("error"))
                .andReturn();

        Object errorAttr = result.getFlashMap().get("error");

        assertThat(errorAttr)
                .isInstanceOf(ExcelReadHandlingException.class)
                .extracting(Object::toString)
                .isEqualTo(error);
    }

    @Test
    void uploadFileShouldThrowWithGroupNotFoundException() throws Exception {
        String errorMessage = "Групу не знайдено";
        String error = "Помилка читання файлу: Групу не знайдено";

        MockMultipartFile file = new MockMultipartFile(
                "file-upl",
                "test.xlsx",
                "application/vnd.ms-excel",
                "data".getBytes());

        when(excelFileCreationService.createExcelTable(file))
                .thenThrow(new GroupNotFoundException(errorMessage));

        mockMvc.perform(multipart("/UploadFile")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/UploadFile"))
                .andExpect(flash().attribute("error", error));
    }

    @Test
    void shouldRedirectUploadFileWhenSuccess() throws Exception {
        byte[] testExcel = "content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file-upl", "test.xlsx", contentType, "enter".getBytes());

        when(excelFileCreationService.createExcelTable(any())).thenReturn(testExcel);

        mockMvc.perform(multipart("/UploadFile")
                        .file(file).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateExcelFile/CreateExcel"))
                .andExpect(model().attributeExists("fileId"));
    }


    // download
    @Test
    void DownloadFile_shouldReturnBadRequestWhenFileNotFound() throws Exception {
        String messageError = "Файл не знайдено або він уже видалений.";
        mockMvc.perform(get("/DownloadFile")
                        .param("fileId", "none"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(messageError));
    }

    @Test
    void downloadFile_shouldReturnFile_whenFileExists() throws Exception {
        String contentDisposition = "attachment; filename=Plan.xlsx";
        File tempFile = File.createTempFile("test", ".xlsx");
        FileUtils.writeByteArrayToFile(tempFile, "content".getBytes());
        String fileId = "idTest";

        Field field = CreateExcelController.class.getDeclaredField("tempFiles");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, File> tempFiles = (Map<String, File>) field.get(controller);

        tempFiles.put(fileId, tempFile);

        mockMvc.perform(get("/DownloadFile")
                        .param("fileId", fileId))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString(contentDisposition)));
    }

}