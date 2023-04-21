let navbarToggleButton = document.getElementById("navbar-toggle-button");
let pageTitle = document.getElementById("page-title");
let memberLogIn = document.getElementById("member-login");
let adminLogIn = document.getElementById("admin-login");
let adminSignUp = document.getElementById("admin-signup");
let recordSearch = document.getElementById("record-search");
const navBar = document.querySelector("nav");
const mainBody = document.querySelector("#main-body");
const header = document.querySelector("header");

let scrollTimeout;
let ws;

// window.onscroll = function () {
//     header.classList.add("scrolled");
//     clearTimeout(scrollTimeout);
//     scrollTimeout = setTimeout(function () {
//         header.classList.remove("scrolled");
//     }, 750);
// };
// window.onscroll = function() {
//     if (window.scrollY){
//     // if (window.pageYOffset > 0){
//         header.classList.add("scrolled");
//     } else {
//         header.classList.remove("scrolled");
//     }
// };

window.onload = function () {
  const windowWidth = window.innerWidth;
  if (windowWidth < 550.0){
    pageTitle.textContent = "Moi Main Altar"
  } else {
    pageTitle.textContent = "Moi University Main Altar"
  }
}

window.onresize = function () {
  const windowWidth = window.innerWidth;
  if (windowWidth < 550.0){
    pageTitle.textContent = "Moi Main Altar"
  } else {
    pageTitle.textContent = "Moi University Main Altar"
  }
}

window.onscroll = function() {
  const windowWidth = window.innerWidth;
  if (windowWidth < 500.0){
    navBar.classList.remove("active");
    mainBody.classList.remove("active");
  }
}

// window.addEventListener("resize", function () {
//   const windowWidth = window.innerWidth;
//   if (windowWidth < 550.0){
//     pageTitle.textContent = "Moi Main Altar"
//   } else {
//       pageTitle.textContent = "Moi University Main Altar"
//   }
// });

navbarToggleButton.addEventListener("click", function () {
  navBar.classList.toggle("active");
  mainBody.classList.toggle("active");
});

memberLogIn.addEventListener("submit", function (event) {
  event.preventDefault();

});

adminLogIn.addEventListener("submit", function (event) {
  event.preventDefault();
});

recordSearch.addEventListener("submit", function (event) {
  event.preventDefault();
  const  formData = new FormData(recordSearch);
  alert(formData.get("search-date"));
  alert("submitted");
});

adminSignUp.addEventListener("submit", function (event) {
  event.preventDefault();
  const formData = new FormData(adminSignUp);
  // ws = new WebSocket("ws://localhost:8080/DemoJ2EE-1.0-SNAPSHOT/api/admin");
  // const data = {
  //     name: formData.get("name-signup"),
  //     email: formData.get("email-signup"),
  //     number: formData.get("number-signup"),
  //     user_name: formData.get("user-name-signup"),
  //     password: formData.get("password-signup"),
  //     security: formData.get("security-signup"),
  //     answer: formData.get("answer-signup")
  // };

  fetch("localhost:8080/DemoJ2EE-1.0-SNAPSHOT/api/admin/SignUp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => {
      console.log("Hello world!");
      console.log(data);
      console.log(response);
      if (response.ok) {
        // Parse the response JSON data
        return response.json().then((theData) => {
          console.log(theData);
          alert(theData);
        });
      } else {
        console.log(response);
        // The response is not successful, so display an error message
        throw new Error("Error submitting data.\n");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Error submitting data: " + error.message);
    });
});
