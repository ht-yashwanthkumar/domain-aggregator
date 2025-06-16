# 🌐 Domain Aggregator

A production-ready Spring Batch application designed to monitor a directory for incoming network log files, aggregate domain usage statistics, and generate periodic reports with the top accessed domains.

---

## 🧩 Key Features

- Monitors a configurable directory for incoming CSV files
- Processes large batches of network connection records
- Ensures duplicate entries within each batch are ignored
- Aggregates top N domains every minute (configurable)
- Outputs results to a timestamped text report
- Built for extensibility and maintainability

---

## 📌 Requirements

- Java 17+
- Maven 3.8+
- Spring Boot 3.x
- Compatible IDE (e.g., IntelliJ, Eclipse)
- Unix/Mac/Windows OS

---

## 🛠️ Architecture Overview

| Component                    | Responsibility                                              |
|-----------------------------|--------------------------------------------------------------|
| `FileIngestionScheduler`    | Periodically scans for new CSV files in input directory      |
| `Spring Batch Job`          | Reads and processes records using chunk-oriented processing  |
| `DuplicateFilterProcessor`  | Filters out duplicates during batch processing               |
| `DomainAggregator`          | Maintains and aggregates domain connection statistics        |
| `DomainReportScheduler`     | Generates the top-N domain report every minute               |

---

## 🗂️ Folder Structure

```bash
domain-aggregator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.domain.aggregator/
│   │   │       ├── batch/                # Spring Batch configuration
│   │   │       ├── model/                # Domain model (e.g., NetworkRecord)
│   │   │       ├── processor/            # Duplicate filtering logic
│   │   │       ├── watcher/              # File and report schedulers
│   │   │       ├── utils/                # Aggregation utilities and constants
│   │   └── resources/
│   │       ├── input/                    # Sample input CSVs
│   │       ├── output/                   # Generated domain reports
│   │       └── application.yml           # Configurable properties
│   └── test/
│       └── ...                           # Unit and integration tests
└── README.md

```
---
📊 Design Highlights
## 🧱 Design Principles
- Code-First approach
- Adherence to SOLID principles
- Robust and graceful error handling mechanism
---
##  📊 Assessment Criteria Coverage
| Criteria                          | Coverage |
| --------------------------------- | -------- |
| Modular Service Components        | ✅        |
| Scheduler + Batch Coordination    | ✅        |
| Custom Domain Aggregation Logic   | ✅        |
| Configuration via YML             | ✅        |
| Clean Code with Logging           | ✅        |
| Duplicate Filtering               | ✅        |
| Periodic Reporting                | ✅        |
| Test Coverage                     | ✅        |

---
## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/ht-yashwanthkumar/domain-aggregator.git
cd domain-aggregator    
```
### 2.Build and Run Services
```bash
mvn clean install
mvn spring-boot:run
```
### 3. Add Input Files
Drop CSV files into the configured input-directory. The system will detect and process them automatically.

### 4. Check Output
Processed results are written to the configured output-file-path. Reports are appended every minute.

---
## 🧪 Testing
Run all unit and integration tests:
```bash
mvn test
```
Tests include:
- Batch job lifecycle
- Duplicate filtering logic
- File polling and job triggering
- Report generation formatting
---
## 📂 Sample Input File Format
```bash
timestamp,src_ip,src_port,dst_ip,dst_port,domain1708867200000,192.168.1.10,5000,10.0.0.5,443,example.com 1708867205000,192.168.1.11,5001,10.0.0.6,80,test.com 1708867210000,192.168.1.12,5002,10.0.0.7,443,example.com
```
## 📤 Sample Output Report
```bash
Top 10 domains - 2025-06-16 12:48:11 1. example.com - 2 connections 2. test.com - 1 connections

```
---
## ⚙️ Configuration

All runtime settings are centralized in `application.yml`.

```yaml
domain-aggregator-config:
  file-ingestion:
    input-directory: ./data/input
    output-file-path: ./data/output/top_domains.txt
    file-read-delay-ms: 2000                # Wait time before assuming file copy is complete
    file-scan-interval-ms: 10000            # Interval to scan input directory

  domain-analytics:
    aggregation-interval-ms: 60000          # Domain report generation interval
    top-domain-limit: 10
    template:  Top %d Domains - %s        # Report header format
```
---
## 🕸️ Sequence Diagram

Visualizing the core flow of the system, here is a sequence diagram illustrating the interactions between components for file ingestion, processing, and report generation.
![image](https://github.com/user-attachments/assets/2a3335b1-45a6-4952-a5fe-c667cd191658)

---

## 🔗 Author
👤 Yashwanth Kumar HT <br/>
📧 ht.yashwanthkumar@gmail.com <br/>
🌐 https://github.com/ht-yashwanthkumar <br/>
📞 +971-582279099 | +91-7353638399 <br/>  
