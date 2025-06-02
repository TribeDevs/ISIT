import "./style.css";
import Header from "../components/Header"

export default function ProfileLayout({ children }) {
  return (
    <html lang="ru">
        <head>
            <meta charSet="UTF-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1"/>
            <title>Профиль</title>
        </head>
        <body className="profile-page">
            {children}
        </body>
    </html>
  );
}
