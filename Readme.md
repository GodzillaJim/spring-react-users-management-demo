# Tracker App

This app is a simple imaginary tracker app to demonstrate user registration using Spring Boot and a React frontend.<br>

The frontend code repo is <a href="https://github.com/GodzillaJim/tracker-frontend.git">here</a>

PostgreSQL is used and the SQL dump is named <code>tracker.sql</code> at the root of the project.

The frontend code is in the <code>/tracker-frontend</code> folder. The frontend uses React/Redux for three pages, <code>/</code> for login, <code>/register</code> and <code>/profile</code> for authenticated users. I used React Bootstrap for the UI components.

## APIs

> > 1.  /api/users/register - POST <code>{ firstName, lastName, email, password, image}</code> to register. Returns <code>{"token": "token value"}</code>, a session token generated.<br/>

> > 2.  /api/users/login - POST <code>{email, password}</code> to generate session tokens. Returns <code>{"token":"token value"}</code><br/>

> > 3.  /api/users/profile - GET, requires Authorization header as Bearer token. Returns <code>{ firstName, lastName, email, image}</code>
