"use client";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { Provider } from "react-redux";
import { store } from "@/lib/redux/store";
import { EventsView } from "@/lib/events-view/events-view";

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <Provider store={store}>
        <body className={inter.className}>
          <div className="flex flex-row justify-between">
            <div className="flex-grow">{children}</div>
            <EventsView />
          </div>
        </body>
      </Provider>
    </html>
  );
}
