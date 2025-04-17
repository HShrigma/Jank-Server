# 🟡 JankServer – Raw Java HTTP Server

A minimal HTTP server built from scratch in pure Java, supporting:

- ✅ GET requests
- ✅ POST file uploads (`application/octet-stream`)
- ✅ File downloads via dynamic route
- ✅ Multithreading with a request limit
- ✅ Basic HTTP error handling

---

## 🏁 Getting Started

### 🧱 Requirements
- Java 17+ (or compatible version)
- Terminal & `curl` for quick testing

### 📂 Directory Structure
<pre>
  src/ 
  └── main/ 
      └── java/ 
      |   └── server/ 
      |       ├── HTTPParser.java 
      |       ├── JankServer.java 
      |       └── Router.java 
      └── resources/ 
          └── uploads/ <-- Uploaded files go here
</pre>

---

## 🚀 Run the Server

```bash
javac src/main/java/server/*.java
java -cp src/main/java server.JankServer
#or just use Maven lol
```
--- 
## 🔁 Supported Endpoints
### GET /

Returns a basic greeting.

```bash
curl http://localhost:8080/
```

### POST /upload
Uploads binary data. You can use any file type.
```bash
curl -X POST --data-binary @file.dat http://localhost:8080/upload
```
Saved files appear in:
```css
src/main/resources/uploads/upload_<timestamp>.dat
```
### GET /upload/<filename>

Downloads a previously uploaded file.
```bash
curl http://localhost:8080/upload/upload_1713200000000.dat -o saved.dat
```
---
## 💡 Notes
- Thread-safe request counting using synchronized
- Thread pool handles multiple simultaneous clients
- All uploads are stored as .dat files
- Content-Type and Content-Length headers sent for downloads
- Only allows 2 total requests before shutting down
  - (for demo purpose, change in ``JankServer.java``, under ``private static final int MAX_REQUESTS = 2;``)

---

 ## 📜 License

Unlicense (public domain)

---
## Author

Yours truly, HShrigma
