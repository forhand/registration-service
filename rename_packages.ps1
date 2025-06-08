# Создаем новые директории
New-Item -ItemType Directory -Force -Path "src/main/java/com/example/registrationservice"
New-Item -ItemType Directory -Force -Path "src/test/java/com/example/registrationservice"

# Копируем все файлы из старых директорий в новые
Copy-Item "src/main/java/com/example/registration_service/*" -Destination "src/main/java/com/example/registrationservice/" -Recurse
Copy-Item "src/test/java/com/example/registration_service/*" -Destination "src/test/java/com/example/registrationservice/" -Recurse

# Заменяем старый пакет на новый во всех файлах
$files = Get-ChildItem -Path "src" -Filter "*.java" -Recurse
foreach ($file in $files) {
    (Get-Content $file.FullName) | 
    ForEach-Object { $_ -replace "com\.example\.registration_service", "com.example.registrationservice" } |
    Set-Content $file.FullName
}

# Удаляем старые директории
Remove-Item "src/main/java/com/example/registration_service" -Recurse -Force
Remove-Item "src/test/java/com/example/registration_service" -Recurse -Force 