---- Lệnh cơ bản của git hay sử dụng
git status --> xem trạng thái - luôn luôn sử dụng trước khi làm bất cứ việc gì

git add . --> add file thêm toàn bộ các file/ thư mục mới vào để commit - hạn chế vì có thể dính xung đột
git add web --> add thư mục cụ thể - nên sử dụng
git add web/src/views/Home.vue --> add file cụ thể - nên sử dụng
git commit -m "remove gitignore" --> commit nội dung khi upcode
git push --> push len
git pull origin main --> tai code ve theo nhánh nào đó
git pull --> check toan bo ve

git checkout origin/main -- api   --> chỉ check thư mục api
git checkout origin/main -- web   --> chỉ check thư mục web
git checkout origin/main -- api/config.json  --> check file cụ thể
git checkout origin/main -- api web  --> check nhiều thư mục

hoặc xem file thay đổi trên git
git fetch --> lay all cac brach tren git ve
git branch -r --> Kiểm tra branch remote
git diff origin/main

git branch --> kiem tra cac brach hien co
