// Данные с сервера
let userGroup = null;
let username = "Участник";

let moneyCount = 5000;
let moneyTotal = 5500;

let tasksCount = 5;
let tasksTotal = 15;


// Получение необходимых элементов со страницы
let usernameEl = document.getElementById("username");
let mapEl = document.getElementById("map");
let bodyEl = document.body;

// Элементы счетчиков ресурсов
let moneyCountEl = document.getElementById("money-count");
let moneyTotalEl = document.getElementById("money-total");

let tasksCountEl = document.getElementById("tasks-count");
let tasksTotalEl = document.getElementById("tasks-total");

// Элементы всплывающего окна
let popupContainerEl = document.getElementById('popup-container');
let closePopupBtn = document.getElementById('popup-close-btn');

let popupHouseNameEl = document.getElementById('popup-house-name');


// Сохраняем идентификатор таймера для всплывающей подсказки
let tooltipTimer;

// Появление подсказки при клике на соответсвующий ресурс
document.querySelectorAll(".resources-group").forEach(function(el) {    
    el.addEventListener("click", function() {
        // Выбираем текст, который будет отображен в посдказке
        let tooltipText;
        switch (el.dataset.tooltip) {
            case "money":
                tooltipText = `Монеты: ${moneyCount.toLocaleString("ru-RU")} активно из ${moneyTotal.toLocaleString("ru-RU")} начисленных.`;
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
        mapEl.style.opacity = 0;

        // Возвращаем прокрутку всплывающего окна к верхней позиции
        popupContainerEl.scrollTop = 0;

        // Отключаем возможность просистывать элемент body
        bodyEl.classList.add("overflow-hidden");

        // Изменение динамического содержимого всплывающего окна
        popupHouseNameEl.textContent = "Название домика";

        // Отображаем всплывающее окно
        popupContainerEl.classList.add('active');
    });
});

// Сокрытие всплывающего окна, показ карты и включение body возможности прокрутки
closePopupBtn.addEventListener('click', function() {
    mapEl.style.opacity = 1;
    bodyEl.classList.remove("overflow-hidden");
    popupContainerEl.classList.remove('active');
});


// Подстановка значений с сервера в соответсвующие элементы на странице
moneyCountEl.innerText = moneyCount.toLocaleString("ru-RU");
moneyTotalEl.innerText = moneyTotal.toLocaleString("ru-RU");

tasksCountEl.innerText = tasksCount;
tasksTotalEl.innerText = tasksTotal;

usernameEl.innerText = username;