# HRIS API Testing

Proyek automated API testing untuk sistem **HRIS (Human Resource Information System)** menggunakan Rest-Assured dan TestNG.

## Tech Stack

| Tool | Versi |
|------|-------|
| Java | 17 |
| Maven | 3.x |
| Rest-Assured | 6.0.0 |
| TestNG | 7.12.0 |
| dotenv-java | 3.2.0 |

---



## Cara Menjalankan Test

### Jalankan semua test

```bash
mvn test
```

### Jalankan satu class saja

```bash
mvn test -Dtest=Department
mvn test -Dtest=Employee
mvn test -Dtest=Position
mvn test -Dtest=RoleManagement
mvn test -Dtest=PermissionManagement
mvn test -Dtest=UserManagement
mvn test -Dtest=UserProfile
mvn test -Dtest=Authorization
```

---

## Command Flow & Dependency Antar Test

> **Penting:** Setiap class test berjalan secara berurutan berdasarkan `priority`. Test dengan priority lebih tinggi **bergantung pada ID** yang dibuat oleh test sebelumnya. Jika test Create/Add (priority 2) dilewati atau gagal, maka test Get By ID, Update, dan Delete **akan ikut gagal** karena ID bernilai `null`.

### Authorization

Tidak ada dependency. Bisa dijalankan sendiri kapan saja.

```
testLoginAdmin (priority 1)
testLoginUser  (priority 2)
```

---

### Department

```
testGetAllDepartments (priority 1)
        ↓
testAddDepartment     (priority 2)  ← menghasilkan departmentId
        ↓
testGetDepartmentById (priority 3)  ← butuh departmentId
        ↓
testUpdateDepartment  (priority 4)  ← butuh departmentId
        ↓
testDeleteDepartment  (priority 5)  ← butuh departmentId
```

> Jika `testAddDepartment` dilewati → `testGetDepartmentById`, `testUpdateDepartment`, `testDeleteDepartment` akan **FAIL** (departmentId = null).

---

### Position

```
testGetPosition     (priority 1)
        ↓
testAddPosition     (priority 2)  ← menghasilkan positionId
        ↓
testGetPositionById (priority 3)  ← butuh positionId
        ↓
testUpdatePosition  (priority 4)  ← butuh positionId
        ↓
testDeletePosition  (priority 5)  ← butuh positionId
```

> Jika `testAddPosition` dilewati → `testGetPositionById`, `testUpdatePosition`, `testDeletePosition` akan **FAIL** (positionId = null).

---

### PermissionManagement

```
testGetPermission     (priority 1)
        ↓
testCreatePermission  (priority 2)  ← menghasilkan permissionId
        ↓
testGetPermissionById (priority 3)  ← butuh permissionId
        ↓
testUpdatePermission  (priority 4)  ← butuh permissionId
        ↓
testDeletePermission  (priority 5)  ← butuh permissionId
```

> Jika `testCreatePermission` dilewati → `testGetPermissionById`, `testUpdatePermission`, `testDeletePermission` akan **FAIL** (permissionId = null).

---

### RoleManagement

> Membutuhkan `permissionId` yang valid di database (saat ini hardcode `"1"`).

```
testGetRole     (priority 1)
        ↓
testCreateRole  (priority 2)  ← menghasilkan roleId
        ↓
testGetRoleById (priority 3)  ← butuh roleId
        ↓
testUpdateRole  (priority 4)  ← butuh roleId
        ↓
testDeleteRole  (priority 5)  ← butuh roleId
```

> Jika `testCreateRole` dilewati → `testGetRoleById`, `testUpdateRole`, `testDeleteRole` akan **FAIL** (roleId = null).

---

### Employee

> Membutuhkan `departmentId` dan `positionId` yang valid di database (saat ini hardcode `"1"`).

```
testGetEmployee     (priority 1)
        ↓
testAddEmployee     (priority 2)  ← menghasilkan employeeId
        ↓
testGetEmployeeById (priority 3)  ← butuh employeeId
        ↓
testUpdateEmployee  (priority 4)  ← butuh employeeId
        ↓
testDeleteEmployee  (priority 5)  ← butuh employeeId
```

> Jika `testAddEmployee` dilewati → `testGetEmployeeById`, `testUpdateEmployee`, `testDeleteEmployee` akan **FAIL** (employeeId = null).

---

### UserManagement

```
testGetUser     (priority 1)
        ↓
testAddUser     (priority 2)  ← menghasilkan userId
        ↓
testGetUserById (priority 3)  ← butuh userId
        ↓
testUpdateUser  (priority 4)  ← butuh userId
        ↓
testDeleteUser  (priority 5)  ← butuh userId
```

> Jika `testAddUser` dilewati → `testGetUserById`, `testUpdateUser`, `testDeleteUser` akan **FAIL** (userId = null).

---

### UserProfile

> Bergantung pada `adminToken` dan `userToken` yang di-generate oleh `BaseTest` saat suite dimulai.

```
testGetProfileAdmin        (priority 1)  ← butuh adminToken
testGetProfileUser         (priority 2)  ← butuh userToken
testGetProfileUnauthorized (priority 3)  ← tidak butuh token (test negatif)
testUpdateProfileAdmin     (priority 4)  ← butuh adminToken + file foto
```

> File foto untuk `testUpdateProfileAdmin` harus tersedia di `src/test/resources/ambatukam.jpeg`.

---

## Urutan Eksekusi yang Direkomendasikan

Jika ingin menjalankan semua test secara manual satu per satu:

```
1. Authorization
2. PermissionManagement
3. RoleManagement
4. Department
5. Position
6. Employee
7. UserManagement
8. UserProfile
```

---

## Struktur Proyek

```
hris-api-testing/
├── src/
│   └── test/
│       ├── java/com/juaracoding/
│       │   ├── BaseTest.java
│       │   ├── Authorization.java
│       │   ├── Department.java
│       │   ├── Employee.java
│       │   ├── PermissionManagement.java
│       │   ├── Position.java
│       │   ├── RoleManagement.java
│       │   ├── UserManagement.java
│       │   └── UserProfile.java
│       └── resources/
│           └── ambatukam.jpeg
├── .env
└── pom.xml
```

---

## Laporan Hasil Test

Setelah `mvn test`, laporan tersedia di:

```
target/surefire-reports/
```
