<p align="center"> 
    <img src="/app/src/main/res/drawable/logo.png" width="250" height="250">
</p>

---

<h3 align="center"> "Leafy" is a Library/Book application </h3>

[Mockup in Figma (with Prototype) 🔗](https://www.figma.com/file/J9oC00repkf14rg8FCGVbr/Leafy?node-id=0%3A1&t=DFxDTsPgKuQeAnCS-1) <br>

[Android PdfViewer 🔗](https://github.com/barteksc/AndroidPdfViewer) <br>

[Glide Library 🔗](https://github.com/bumptech/glide) <br>

All authentication, registration and data operations are done with **"Firebase"** <br>

"Material Design Kit", "Material Design Icons" and various plugins were used in the design. <br>

---

### Login

- Admin

Email and Password:
```
admin@example.com
123456
```

<br>

- User

Email and Password:
```
emre@example.com
123456
```


---

### App Features:

- The application is developed with "Kotlin" in "Android Studio".
- You can create an account and log in. (There is an option to use the app without creating an account)
- There is a button to log out of the account in the User Dashboard panels. 
- The account you are logged into goes through verification processes.
- If you forget your password, you can reset your password.
- If you have logged into the application before, this information will be checked on the "Splash" screen and you will be logged in automatically.
- There are two types of users in the application, "User" and "Admin".
- Each user has a separate dashboard according to their own authority.
- "Admin" can add book categories, upload these books as PDF, search and delete any book.
- "Users" can view the books (clicking on the book will show the details of the book), download these books as PDF and search,
can also create a favorite book list by adding them to favorites.
- A profile page is available for each user. User info, user profile picture and favorite books are on this page.
- Users are informed with "Toast" messages and "Progress Dialog" in almost every action they take,
relevant warning messages are also given where incorrect entries are made.
