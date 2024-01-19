// Данные с сервера
let userGroup = null;
let username = "Света";

let moneyCount = 5000;
let moneyTotal = 5500;

let ticketsCount = 6000;
let ticketsTotal = 7000;

let tasksCount = 5;
let tasksTotal = 15;

// Получение необходимых элементов со страницы
let usernameEl = document.getElementById("username");

// Элементы счетчиков ресурсов
let moneyCountEl = document.getElementById("money-count");
let moneyTotalEl = document.getElementById("money-total");

let ticketsCountEl = document.getElementById("tickets-count");
let ticketsTotalEl = document.getElementById("tickets-total");

let tasksCountEl = document.getElementById("tasks-count");
let tasksTotalEl = document.getElementById("tasks-total");

// Элементы всплывающего окна
let popupContainerEl = document.getElementById('popupContainer');
let popupContentEl = document.getElementById('popupContent');
let closePopupBtn = document.getElementById('closePopupBtn');


// Обработка кликов при нажатии на каждый из домов
document.querySelectorAll(".house").forEach(function(el) {
    el.addEventListener('click', function() {
        // Определение динамического содержимого всплывающего окна
        popupContentEl.textContent = `Это Дом под номером ${el.dataset.house}`;

        // Отображаем всплывающее окно
        popupContainerEl.classList.add('active');
    });
});

// Сокрытие всплывающего окна
closePopupBtn.addEventListener('click', function() {
    popupContainer.classList.remove('active');
});


// Появление подсказки при клике на соответсвующий ресурс
document.querySelectorAll(".resources-group").forEach(function(el) {
    el.addEventListener("click", function() {
        // Выбираем текст, который будет отображен в подсказке
        let tooltipText;
        switch (el.dataset.tooltip) {
            case "money":
                tooltipText = `${moneyCount} монет активно из ${moneyTotal} начисленных.`;
                break;
            case "tickets":
                tooltipText = `${ticketsCount} билетов активно из ${ticketsTotal} начисленных.`;
                break;
            case "tasks":
                tooltipText = `${tasksCount} заданий выполнено из ${tasksTotal} доступных.`;
        }

        // Вставляем выбранный текст в подсказку
        tooltip.innerText = tooltipText;
        
        // Показываем подсказку
        tooltip.style.opacity = "1";
        
        // Скрываем подсказку через 3 секунды
        setTimeout(function() {
            tooltip.style.opacity = "0";
        }, 3000);
    });
});


// Подстановка значений с сервера в соответсвующее элементы на странице
moneyCountEl.innerText = moneyCount;
ticketsCountEl.innerText = ticketsCount;
tasksCountEl.innerText = tasksCount;

moneyTotalEl.innerText = moneyTotal;
ticketsTotalEl.innerText = ticketsTotal;
tasksTotalEl.innerText = tasksTotal;

usernameEl.innerText = username;