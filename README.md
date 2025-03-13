# Privacy KTA

Ứng dụng Privacy KTA là một module Xposed giúp ẩn danh sách ứng dụng đã cài đặt khỏi các ứng dụng khác, bảo vệ quyền riêng tư của bạn.

## Tính năng

- **Ứng dụng ẩn**: Ẩn các ứng dụng đã chọn khỏi danh sách ứng dụng đã cài đặt
- **Danh sách trắng**: Cho phép một số ứng dụng nhất định xem toàn bộ danh sách ứng dụng
- **Danh sách đen**: Chặn hoàn toàn một số ứng dụng nhất định không cho xem bất kỳ ứng dụng nào
- **Kiểm tra trạng thái**: Kiểm tra xem module có hoạt động không và xem nhật ký lọc

## Yêu cầu

- Android 10+ (API 29+)
- LSPosed, EdXposed hoặc các framework Xposed tương thích khác

## Cài đặt

1. Cài đặt LSPosed hoặc framework Xposed tương thích
2. Cài đặt ứng dụng Privacy KTA
3. Kích hoạt module trong LSPosed Manager
4. Chọn "android" làm phạm vi (scope)
5. Khởi động lại thiết bị
6. Mở ứng dụng Privacy KTA và cấu hình các ứng dụng bạn muốn ẩn

## Cách sử dụng

1. **Ứng dụng ẩn**: Chọn các ứng dụng bạn muốn ẩn khỏi danh sách ứng dụng đã cài đặt
2. **Danh sách trắng**: Chọn các ứng dụng được phép xem toàn bộ danh sách ứng dụng
3. **Danh sách đen**: Chọn các ứng dụng không được phép xem bất kỳ ứng dụng nào
4. **Cài đặt**: Kiểm tra trạng thái module, đặt lại cấu hình và các tùy chọn khác

## Giấy phép

Dự án này được phân phối dưới giấy phép GPL-3.0.

## Tín dụng

Dự án này lấy cảm hứng từ Hide My Applist và các dự án tương tự. 