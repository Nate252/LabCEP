# CreateEstimatingPlan


## Керівництво щодо розгортання
Для встановлення програмного забезпечення необхідно виконати такі кроки:


### Встановлення Java

1. Перейдіть за посиланням  [Java SE Development Kit](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
2. Оберіть та завнтажте версію в залежності від обраної операційної системи.
3. Установіть Java SE Development Kit.
4. Перевірьте результат встановлення відкривши cmd та ввівши команду `java -–version`.
   
### Встановлення Apache Maven

1. Перейдіть за посиланням та слідуйте вказівкам [Apache Maven](https://www.youtube.com/watch?v=Br98iO1K1SA).
   
### Встановлення Tomcat

1.  Перейдіть за посиланням
    * Ubuntu https://www.youtube.com/watch?v=q07SV9aRA7U
    * macOC https://www.youtube.com/watch?v=5J9Elf1tzO0
    * Windows https://www.youtube.com/watch?v=d8BAQ_zRmYY

### Створення бази даних за допомогою дампу
1. Відкрийте MySQL Workbench.
2. Здійсніть підключення.
3. У випадаючому мені Server оберіть Data Import.
4. Оберіть Import from Dump Project Folder.
5. Натисніть … та оберіть шлях до папки з дампом DB.
6. Також оберіть у випадаючому списку Dump Structure and Datа.
7. Натисність кнопку Start Import.
8. Оновіть список схем та перевірьте наявність бази даних.

### Збирання проєкту
1. Перейдіть в корень проєкту з назвою CreateEstimatingPlan та виконайте команду:
    * Windows `mvnw.cmd clean package`.
    * macOS/Ubuntu `/mvnw clean package`.
2. Це створить CreateEstimatingPlan.war файл у директорії target/.

### Розгортання на Tomcat:
1. Скопіювати CreateEstimatingPlan.war файл до папки Tomcat/webapps.
2. Перейти в папку Tomcat/bin.
    * Якщо сервер запущено виконати shutdown.bat.
    * Якщо сервер не запущено виконати startup.bat.
3. Тепер ви можете перейти за адресою http://localhost:????/CreateEstimatingPlan/home на сторінку веб-сервісу. Замість ? вкажіть номер порту який вказали при встановленні Tomcat.
