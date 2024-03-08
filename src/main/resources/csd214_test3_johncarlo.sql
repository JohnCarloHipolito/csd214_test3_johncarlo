-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mariadb
-- Generation Time: Mar 08, 2024 at 07:34 PM
-- Server version: 11.3.2-MariaDB-1:11.3.2+maria~ubu2204
-- PHP Version: 8.2.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `csd214_test3_johncarlo`
--

-- --------------------------------------------------------

--
-- Table structure for table `pizza_order`
--

CREATE TABLE `pizza_order` (
  `id` int(11) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `mobile_number` varchar(64) NOT NULL,
  `pizza_size` varchar(8) NOT NULL,
  `no_of_toppings` int(11) NOT NULL,
  `total_bill` decimal(16,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pizza_order`
--

INSERT INTO `pizza_order` (`id`, `customer_name`, `mobile_number`, `pizza_size`, `no_of_toppings`, `total_bill`) VALUES
(1, 'Customer One', '123-456-7890', 'S', 3, 14.37),
(2, 'Customer Two', '450-123-4563', 'M', 0, 11.50),
(3, 'Customer Three', '184-654-9801', 'M', 2, 14.95),
(4, 'Customer Four', '586-432-6095', 'L', 7, 25.87),
(5, 'Customer Five', '454-332-7651', 'XL', 4, 24.15);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `pizza_order`
--
ALTER TABLE `pizza_order`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pizza_order`
--
ALTER TABLE `pizza_order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
