import { Roboto } from "next/font/google";
import "@/globals.css";
import Header from '@/components/Header';
import { AuthProvider } from "@/context/AuthContext";

const roboto = Roboto({
  weight: '300', 
  subsets: ['latin', 'cyrillic'], 
  display: 'swap', 
});

export default function RootLayout({ children }) {
  return (
    <html lang="en" className={roboto.className}>
      <body className={roboto.className}>
        <AuthProvider>
          <Header/>
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
