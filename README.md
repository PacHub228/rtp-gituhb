# 🌍 RTP — Random Teleport Plugin

> Minecraft Paper плагин для случайной телепортации с GUI меню, кулдауном и поддержкой нескольких языков.

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.x-brightgreen)
![Paper](https://img.shields.io/badge/API-Paper-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 📋 Возможности

- 🎲 Случайная телепортация в безопасное место в обычном мире
- 🖥️ GUI меню на основе сундука
- ⏱️ Кулдаун между телепортациями (настраивается)
- 🏠 Телепортация на спавн (`/rtp spawn`)
- 🌐 Поддержка языков: **RU / EN / PL / UK**
- ⚙️ Гибкая настройка через `config.yml`
- ✅ Безопасная ТП — проверка твёрдого блока под ногами, отсутствия воды и лавы

---

## 🚀 Установка

1. Скачай последний `.jar` из [Releases](../../releases)
2. Положи в папку `plugins/` своего сервера
3. Перезапусти сервер
4. Настрой `plugins/rtp-github/config.yml`

**Требования:** Paper 1.21+, Java 21+

---

## ⌨️ Команды

| Команда | Описание | Привилегия |
|---|---|---|
| `/rtp` | Открыть меню телепортации | `rtp.user` |
| `/rtp help` | Список всех команд | — |
| `/rtp spawn` | Телепортироваться на спавн | `rtp.user` |
| `/rtp setspawn` | Установить спавн на текущую позицию | `rtp.admin` |

---

## 🔑 Привилегии

| Привилегия | Описание | По умолчанию |
|---|---|---|
| `rtp.user` | Доступ к телепортации (радиус ±5000 блоков, кулдаун 15 сек.) | `op` |
| `rtp.admin` | Все возможности, без кулдауна, доступ к `/rtp setspawn` | `op` |

---

## ⚙️ Конфигурация

`plugins/rtp-github/config.yml`

```yaml
# Язык плагина: ru, en, pl, uk
language: ru

# Координаты спавна
spawn:
  world: world
  x: 0.5
  y: 64.0
  z: 0.5
  yaw: 0.0
  pitch: 0.0

# Настройки телепортации
rtp:
  max-radius: 5000   # Максимальный радиус от 0,0 (блоки)
  min-radius: 100    # Минимальный радиус от 0,0 (блоки)
  cooldown: 15       # Кулдаун между ТП (секунды)
  max-attempts: 50   # Попыток найти безопасное место
```

---

## 🌐 Языки

Языковые файлы находятся в `plugins/rtp-github/lang/`. После первого запуска их можно редактировать.

| Код | Язык |
|---|---|
| `ru` | Русский |
| `en` | English |
| `pl` | Polski |
| `uk` | Українська |

Для смены языка измени `language` в `config.yml` и перезапусти сервер.

---

## 🏗️ Сборка из исходников

```bash
git clone https://github.com/your-username/rtp-github.git
cd rtp-github
mvn clean package
```

Готовый `.jar` будет в папке `target/`.

---

## 📁 Структура проекта

```
src/main/java/pac/chromium/rtpGithub/
├── RtpGithub.java              # Главный класс плагина
├── command/
│   └── RtpCommand.java         # Обработка команд
├── config/
│   ├── ConfigManager.java      # Загрузка config.yml
│   └── LangManager.java        # Загрузка языковых файлов
├── gui/
│   └── RtpGui.java             # GUI меню
└── manager/
    └── RtpManager.java         # Логика ТП и кулдаун

src/main/resources/
├── config.yml
├── plugin.yml
└── lang/
    ├── ru.yml
    ├── en.yml
    ├── pl.yml
    └── uk.yml
```

---

## 📜 Лицензия

[MIT](LICENSE)
