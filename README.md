# 🦅 A.R.C.S. (Aerial Rescue & Color Search System)

![Java](https://img.shields.io/badge/Language-Java-orange)
![Library](https://img.shields.io/badge/Library-JavaCV%20%2F%20OpenCV-blue)
![Status](https://img.shields.io/badge/Status-Prototype-green)
![License](https://img.shields.io/badge/License-Academic-lightgrey)

> **"The Second Eye for Search & Rescue Operations."**

## 📖 About The Project

**A.R.C.S.** (Aerial Rescue & Color Search System) adalah sebuah purwarupa perangkat lunak (*software prototype*) yang dikembangkan untuk membantu operasi pencarian korban bencana (SAR), khususnya pada kasus kecelakaan air (*Laka Air*) dan bencana alam.

Sistem ini dirancang untuk mengatasi keterbatasan visual operator drone manusia yang sering mengalami kelelahan mata (*visual fatigue*) saat menyisir area luas. Menggunakan algoritma **Computer Vision**, A.R.C.S. mampu mendeteksi objek spesifik (seperti pelampung/life vest) secara otomatis dan melacak pergerakannya di perairan.

Project ini dikembangkan sebagai studi kasus penerapan **Object Oriented Programming (OOP)** dan integrasi sensor pada simulasi UAV.

---

## 🚀 Key Features

✅ **Smart Color Detection**
Mendeteksi objek berdasarkan spektrum warna HSV (Hue, Saturation, Value) spesifik, dioptimalkan untuk warna oranye/merah (Life Vest standar BPBD).

✅ **Flexible Input Source**
Mendukung input dari Webcam USB, IP Camera (RTSP/HTTP), maupun file video rekaman (untuk simulasi pasca-bencana), Ataumun fstream.

---

## 🛠️ Tech Stack

* **Language:** Java (JDK 17+)
* **Computer Vision Library:** [JavaCV](https://github.com/bytedeco/javacv) (Wrapper for OpenCV)
* **IDE:** IntelliJ IDEA
* **Build Tool:** Maven
* **Concepts:** Inheritance, Polymorphism, Encapsulation (OOP)

---
