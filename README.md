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
          └── server/ 
              ├── HTTPParser.java 
              ├── JankServer.java 
              └── Router.java 
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
