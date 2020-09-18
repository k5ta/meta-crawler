# meta crawler

Простой краулер для сбора метаинформации с сайтов (title, meta keywords, meta description).

Сохранение данных - в csv-формате.

## Сборка и запуск

Сборка - командой sbt assembly, запуск - 
> java -Dconfig.file=application.conf -Dlog4j.configurationFile=log4j2.xml -jar MetaCrawler-assembly-1.0.jar

## Что хотелось бы добавить, но пока не добавлено
* Сохранение в файл батчами, а не по одной записи
* Улучшить логирование
* Тесты!