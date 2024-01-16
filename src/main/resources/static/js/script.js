// Данные с сервера
let userGroup = null;
let username = "Света";
let moneyCount = 5;
let ticketsCount = 6;

// Получение необходимых элементов со страницы
let usernameEl = document.getElementById("username");
let housesEl = document.querySelectorAll(".house")

// Элементы счетчиков ресурсов
let moneyCountEl = document.getElementById("money-count");
let ticketsCountEl = document.getElementById("tickets-count");

// Элементы всплывающего окна
let popupContainerEl = document.getElementById('popupContainer');
let popupContentEl = document.getElementById('popupContent');
let closePopupBtn = document.getElementById('closePopupBtn');

// Обработка кликов при нажатии на каждый из домов
housesEl.forEach(function(el) {
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

// Подстановка значений с сервера в соответсвующие элементы на странице
moneyCountEl.innerText = moneyCount;
ticketsCountEl.innerText = ticketsCount;
usernameEl.innerText = username;