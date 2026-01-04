-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 04, 2026 lúc 02:09 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `cuongthuanstore`
--
CREATE DATABASE IF NOT EXISTS `cuongthuanstore` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `cuongthuanstore`;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `brands`
--

CREATE TABLE `brands` (
  `brand_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `origin` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `brands`
--

INSERT INTO `brands` (`brand_id`, `name`, `origin`) VALUES
(1, 'Apple', 'USA'),
(2, 'Samsung', 'Korea'),
(3, 'Dell', 'USA'),
(4, 'Sony', 'Japan'),
(5, 'Logitech', 'Switzerland');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart`
--

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cart`
--

INSERT INTO `cart` (`cart_id`, `user_id`, `product_id`, `quantity`, `added_at`) VALUES
(1, 3, 1, 1, '2026-01-03 08:49:50'),
(2, 3, 4, 2, '2026-01-03 08:49:50'),
(3, 6, 2, 1, '2026-01-03 08:49:50');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`category_id`, `name`, `description`) VALUES
(1, 'Laptop', 'Các dòng máy tính xách tay văn phòng, gaming'),
(2, 'Smartphone', 'Điện thoại thông minh các hãng'),
(3, 'Tablet', 'Máy tính bảng phục vụ làm việc và giải trí'),
(4, 'Accessory', 'Phụ kiện: Chuột, bàn phím, tai nghe'),
(5, 'Audio', 'Thiết bị âm thanh: Loa, tai nghe Bluetooth');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `notification_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `notifications`
--

INSERT INTO `notifications` (`notification_id`, `user_id`, `title`, `message`, `image`, `created_at`) VALUES
(1, 3, '🔥 SIÊU SALE NĂM MỚI 2026', 'Lì xì ngay 500k khi mua MacBook Air M2. Số lượng có hạn, chốt ngay!', 'macbook_sale.jpg', '2026-01-03 04:00:42'),
(2, 6, '⚡ FLASH SALE GIỜ VÀNG', 'Phụ kiện Sony, Logitech đồng loạt giảm 30% chỉ trong khung giờ 12h-14h hôm nay.', 'accessories_flash.png', '2026-01-03 04:00:42'),
(3, 4, '🎁 QUÀ TẶNG ĐẶC BIỆT', 'Mua Galaxy S23 Ultra tặng kèm bộ sạc nhanh 45W và ốp lưng cao cấp.', 'samsung_gift.jpg', '2026-01-03 04:00:42'),
(4, 5, '💻 DEAL HỜI LAPTOP DELL', 'Dell XPS 13 giảm giá sập sàn, tặng thêm balo chống nước xịn xò.', 'dell_xps_promo.jpg', '2026-01-03 04:00:42'),
(5, 3, '🎫 VOUCHER GIẢM 10%', 'Dành riêng cho bạn: Nhập mã CTSTORE10 để được giảm thêm 10% cho đơn hàng tiếp theo.', 'voucher_10.png', '2026-01-03 04:00:42');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `total_amount` decimal(15,2) NOT NULL,
  `status` enum('pending','confirmed','shipping','delivered','cancelled') DEFAULT 'pending',
  `shipping_address` text NOT NULL,
  `payment_method` varchar(50) DEFAULT 'COD',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `name`, `total_amount`, `status`, `shipping_address`, `payment_method`, `created_at`) VALUES
(1, 3, 'Lê Văn Khách', 28000000.00, 'delivered', '789 CMT8, Hà Nội', 'Banking', '2026-01-02 11:13:39'),
(2, 4, 'Phạm Minh Anh', 22500000.00, 'shipping', '321 Lý Tự Trọng, Đà Nẵng', 'COD', '2026-01-02 11:13:39'),
(3, 5, 'Hoàng Thị Lan', 37500000.00, 'pending', '654 Trần Hưng Đạo, Cần Thơ', 'COD', '2026-01-02 11:13:39'),
(4, 3, 'Lê Văn Khách', 17000000.00, 'confirmed', '789 CMT8, Hà Nội', 'Banking', '2026-01-02 11:13:39'),
(5, 4, 'Phạm Minh Anh', 2500000.00, 'cancelled', '321 Lý Tự Trọng, Đà Nẵng', 'COD', '2026-01-02 11:13:39'),
(6, NULL, 'Nguyễn Văn Khách', 1500000.00, 'pending', '123 Đường ABC, Quận 1, HCM', 'COD', '2026-01-03 12:15:30');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_details`
--

CREATE TABLE `order_details` (
  `detail_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_purchase` decimal(15,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_details`
--

INSERT INTO `order_details` (`detail_id`, `order_id`, `product_id`, `quantity`, `price_at_purchase`) VALUES
(1, 1, 1, 1, 28000000.00),
(2, 2, 2, 1, 22500000.00),
(3, 3, 3, 1, 35000000.00),
(4, 3, 5, 1, 2500000.00),
(5, 4, 4, 2, 8500000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `supplier_id` int(11) DEFAULT NULL,
  `price` decimal(15,2) NOT NULL,
  `stock_quantity` int(11) DEFAULT 0,
  `description` text DEFAULT NULL,
  `thumbnail` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`product_id`, `name`, `category_id`, `brand_id`, `supplier_id`, `price`, `stock_quantity`, `description`, `thumbnail`) VALUES
(1, 'MacBook Air M2', 1, 1, 1, 28000000.00, 15, 'Màn hình Liquid Retina, Chip M2 cực mạnh', 'macbook_m2.jpg'),
(2, 'Galaxy S23 Ultra', 2, 2, 3, 22500000.00, 20, 'Camera 200MP, Spen thần thánh', 's23_ultra.jpg'),
(3, 'Dell XPS 13', 1, 3, 2, 35000000.00, 10, 'Thiết kế cao cấp, mỏng nhẹ', 'dell_xps.jpg'),
(4, 'Sony WH-1000XM5', 5, 4, 4, 8500000.00, 30, 'Chống ồn chủ động đỉnh cao', 'sony_xm5.jpg'),
(5, 'Chuột Logitech MX Master 3S', 4, 5, 5, 2500000.00, 50, 'Chuột công thái học tốt nhất', 'mx_master3s.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `name`, `phone_number`, `email`, `address`) VALUES
(1, 'FPT Wholesale', '18005566', 'wholesale@fpt.com.vn', 'TP.HCM'),
(2, 'FPT Wholesale', '18005566', 'wholesale@fpt.com.vn', 'Đà Nẵng'),
(3, 'Thế Giới Số', '028334455', 'info@thegioiso.com', 'Hải Phòng'),
(4, 'Công ty Viễn Sơn', '028383344', 'support@vienson.vn', 'Thanh Hóa'),
(5, 'Dầu Khí (PST)', '024377221', 'sales@pst.com.vn', 'Hà Nội');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `gender` enum('Nam','Nữ','Khác') DEFAULT NULL,
  `role` enum('admin','staff','customer') NOT NULL DEFAULT 'customer',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `email`, `password_hash`, `phone_number`, `address`, `gender`, `role`, `created_at`) VALUES
(1, 'Nguyễn Văn Minh', 'admin', 'hehe123', '0985772330', 'Hà Nội', 'Nam', 'admin', '2026-01-02 11:13:39'),
(2, 'Trần Thị Nhân Viên', 'staff', 'staff123', '0907654321', '456 Nguyễn Huệ, TP.HCM', 'Nữ', 'customer', '2026-01-02 11:13:39'),
(3, 'Lê Văn Khách', 'khach1@gmail.com', 'hash789', '0912345678', '789 CMT8, Hà Nội', 'Nam', 'customer', '2026-01-02 11:13:39'),
(4, 'Phạm Minh Anh', 'minhanh@gmail.com', 'hashabc', '0922334455', '321 Lý Tự Trọng, Đà Nẵng', 'Khác', 'customer', '2026-01-02 11:13:39'),
(5, 'Hoàng Thị Lan', 'lanhoang@gmail.com', 'hashxyz', '0933445566', '654 Trần Hưng Đạo, Cần Thơ', 'Nữ', 'customer', '2026-01-02 11:13:39'),
(6, 'minh', 'minh@gmail.com', 'minh123', '0985772330', 'Hà Nội', 'Nam', 'customer', '2026-01-02 18:59:13'),
(9, 'Nguyễn Khách Hàng', 'customer@gmail.com', '123456', '0999888777', 'Hà Nội', 'Nam', 'customer', '2026-01-03 08:41:27'),
(10, 'Trần Thị Thu Thảo', 'thao_staff@cuongthuan.com', '123456', '0988777666', 'Hà Nội', 'Nữ', 'staff', '2026-01-03 08:44:09');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `brands`
--
ALTER TABLE `brands`
  ADD PRIMARY KEY (`brand_id`);

--
-- Chỉ mục cho bảng `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `category_id` (`category_id`),
  ADD KEY `brand_id` (`brand_id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Chỉ mục cho bảng `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `brands`
--
ALTER TABLE `brands`
  MODIFY `brand_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `notifications`
--
ALTER TABLE `notifications`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `order_details`
--
ALTER TABLE `order_details`
  MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`),
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`),
  ADD CONSTRAINT `products_ibfk_3` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
