// Получаем содержимое JSON из элемента с id "jsonData"
let jsonData = JSON.parse(document.getElementById('jsonData').textContent);


// Парсим JSON и присваиваи данные из него соотвествующим переменным
let username = jsonData.user.username;

let tasksCount = jsonData.user.tasksCount;
let tasksTotal = jsonData.user.tasksTotal;

let moneyCount = jsonData.user.coins;
let moneyTotal = jsonData.user.maxCoins;


// Получение необходимых элементов со страницы
let usernameEl = document.getElementById("username");
let playgroundEl = document.getElementById("playground");
let mapEl = document.getElementById("map");
let bodyEl = document.body;

// Элементы счетчиков ресурсов
let moneyCountEl = document.getElementById("money-count");
let moneyTotalEl = document.getElementById("money-total");

let tasksCountEl = document.getElementById("tasks-count");
let tasksTotalEl = document.getElementById("tasks-total");

// Элементы всплывающего окна
let popupContainerEl = document.getElementById('popup-container');
let popupBodyEl = document.getElementById('popup-body');
let popupCloseBtnEl = document.getElementById('popup-close-btn');


let popupHouseImgEl = document.getElementById('popup-house-img');
let popupHouseNameEl = document.getElementById('popup-house-name');
let popupHouseDescriptionEl = document.getElementById('popup-house-description');
let popupHouseTaskDescriptionEl = document.getElementById('popup-house-task-description');


// Смена карты в зависимости от количества домов в JSON
switch (Object.keys(jsonData.map).length) {
    case 6:
        bodyEl.classList.add("map-6");
        mapEl.src = "/townofgames/img/maps/map6.jpg";
        break;
    case 9:
        bodyEl.classList.add("map-9");
        mapEl.src = "/townofgames/img/maps/map9.jpg";
}


// Создание блоков домов в соответсвии с данными из JSON
for (var key in jsonData.map) {
    // Определение иконки над домом
    let houseStatusIconSrc;
    switch (jsonData.map[key].taskStatus) {
        case "AVAILABLE":
            houseStatusIconSrc = "/townofgames/img/icons/coin.png";
            break;
        case "COMPLETE":
            houseStatusIconSrc = "/townofgames/img/icons/check.png";
            break;
        case "EMPTY":
            houseStatusIconSrc = null;
    }

    // Строка с HTML-разметкой блока дома
    let htmlString = `
        <div class="house house-${key}" data-house="${key}">
            <img class="house-status-icon ${houseStatusIconSrc ? "" : "display-none"}" src="${houseStatusIconSrc}">
            <div class="house-name-block">${jsonData.map[key].name}</div>
        </div>
    `;

    // Создаем временный элемент (div) для парсинга строки в DOM
    let tempElement = document.createElement('div');
    tempElement.innerHTML = htmlString;

    // Получаем созданный элемент из временного контейнера
    let houseEl = tempElement.firstElementChild;

    // Добавляем созданный элемент на карту
    playgroundEl.appendChild(houseEl);
}


// Сохраняем идентификатор таймера для всплывающей подсказки
let tooltipTimer;

// Появление подсказки при клике на соответсвующий ресурс
document.querySelectorAll(".resources-group").forEach(function(el) {    
    el.addEventListener("click", function() {
        // Выбираем текст, который будет отображен в посдказке
        let tooltipText;
        switch (el.dataset.tooltip) {
            case "money":
                tooltipText = `Монеты: ${moneyCount.toLocaleString("ru-RU")} начислено из ${moneyTotal.toLocaleString("ru-RU")} доступных.`;
                break;
            case "tasks":
                tooltipText = `Задания: ${tasksCount} выполнено из ${tasksTotal} доступных.`;
        }

        // Вставляем выбранный текст в подсказку
        tooltip.innerText = tooltipText;
        
        // Показываем подсказку
        tooltip.style.opacity = 1;
        
        // Отменяем предыдущий таймер перед установкой нового
        clearTimeout(tooltipTimer);

        // Устанавливаем новый таймер для скрытия подсказки через 2.5 секунды
        tooltipTimer = setTimeout(function() {
            tooltip.style.opacity = 0;
        }, 2500);
    });
});


// Обработка кликов при нажатии на каждый из домов
document.querySelectorAll(".house").forEach(function(el) {
    el.addEventListener('click', function() {
        // Скрываем карту
        playgroundEl.style.opacity = 0;

        // Возвращаем прокрутку всплывающего окна к верхней позиции
        popupContainerEl.scrollTop = 0;

        // Отключаем возможность пролистывать элемент body
        bodyEl.classList.add("overflow-hidden");

        // Очищаем все содержимое тела всплывающего окна, чтобы наполнить его данными от нового дома
        popupBodyEl.innerHTML = "";

        // Получаем объект дома, по которому был произведен клик
        let houseObject = jsonData.map[el.dataset.house];

        // Изменение динамического содержимого всплывающего окна
        popupHouseImgEl.src = `/townofgames/img/houses/${el.dataset.house}.jpg`;
        popupHouseNameEl.textContent = houseObject.name;
        popupHouseDescriptionEl.innerHTML = houseObject.description;      
        
        // Отображение дополнительного описания
        if (houseObject.taskDescriptionStringMap) {
            popupHouseTaskDescriptionEl.classList.remove("display-none");
            popupHouseTaskDescriptionEl.innerHTML = houseObject.taskDescriptionStringMap;
        } else {
            popupHouseTaskDescriptionEl.classList.add("display-none");
        }

        // Отображение задач, если задачи были переданы
        if (houseObject.tasks) {
            for (let task of houseObject.tasks) {
                // Создаем элемент <div>
                let popupTaskBlockEl = document.createElement('div');

                // Добавляем класс к созданному элементу
                popupTaskBlockEl.classList.add('popup-task-block');

                // Устанавливаем текстовое содержимое
                popupTaskBlockEl.textContent = task.description;

                // Если задача выполненна - отображаем это
                if (task.isComplete) {
                    let popupTaskStatus = document.createElement('div');
                    popupTaskStatus.classList.add('popup-task-status');

                    let imgEl = document.createElement('img');
                    imgEl.setAttribute('src', '/townofgames/img/check.png');

                    popupTaskStatus.appendChild(imgEl);
                    popupTaskStatus.appendChild(document.createTextNode('Выполнено'));

                    popupTaskBlockEl.appendChild(popupTaskStatus);
                }

                // Добавляем созданный элемент в тело всплывающего окна
                popupBodyEl.appendChild(popupTaskBlockEl);
            }
        }

        // Отображение кнопок, если кнопки были переданы
        if (houseObject.buttons) {
            for (let button of houseObject.buttons) {
                let popupButtonEl = document.createElement("a");
                popupButtonEl.classList.add("popup-button");
                popupButtonEl.href = button.url;
                popupButtonEl.innerText = button.text;
                popupButtonEl.target = '_blank';

                // Добавляем созданный элемент в тело всплывающего окна
                popupBodyEl.appendChild(popupButtonEl);
            }
        }

        // Отображение сноски, если сноска была передана
        if (houseObject.caption) {
            let popupFootnoteEl = document.createElement('p');
            popupFootnoteEl.classList.add("popup-footnote");
            popupFootnoteEl.innerHTML = houseObject.caption;

            // Добавляем созданный элемент в тело всплывающего окна
            popupBodyEl.appendChild(popupFootnoteEl);            
        }

        // Отображаем всплывающее окно
        popupContainerEl.classList.add('active');
    });
});

// Сокрытие всплывающего окна при клике на кнопку "<- НАЗАД, показ карты и включение body возможности прокрутки
popupCloseBtnEl.addEventListener('click', function() {
    popupContainerEl.classList.remove('active');
    playgroundEl.style.opacity = 1;
    bodyEl.classList.remove("overflow-hidden");
});


// Подстановка значений с сервера в соответсвующие элементы на странице
moneyCountEl.innerText = moneyCount.toLocaleString("ru-RU");
moneyTotalEl.innerText = moneyTotal.toLocaleString("ru-RU");

tasksCountEl.innerText = tasksCount;
tasksTotalEl.innerText = tasksTotal;

usernameEl.innerText = username;