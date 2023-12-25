Имитация системы управления лифтами в здании.
Есть система управления с этажами и лифтами, которая:
1. обрабатывает появление на этажах групп людей
2. осуществляет поиск лифта (на данный момент неоптимально)
3. осуществляет вызов лифта, помещение группы в лифт и доставку на нужный этаж.

За основу взята реализованная ранее версия, сделан рефакторинг.
Реализован IoC-контейнер с регистрацией зависимостей (с возможностью одновременного существования нескольких контейнеров).

Реализована простая логика распределения "заданий" по лифтам.
В процессе доработка логики, переход на более продвинутый расчет нужного лифта с системой штрафов и сбором статистики, анализом аглогитмов.

Запуск: 
Core.App - точка входа. Конфигурируется:
Init.initialLoad(10,3,100);// количество этажей, количество лифтов, максимальная вместимость лифта.
Метод main, по сути, для тестирования.
