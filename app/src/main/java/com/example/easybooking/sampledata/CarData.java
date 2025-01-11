package com.example.easybooking.sampledata;

import com.example.easybooking.models.Car;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CarData {
    public static ArrayList<Car> getExampleCarList() {
        ArrayList<Car> carList = new ArrayList<>();

        carList.add(new Car("1", "https://media.istockphoto.com/id/1150931120/vi/anh/minh-họa-3d-của-chiếc-xe-nhỏ-gọn-chung-màu-trắng-front-side-view.jpg?s=1024x1024&w=is&k=20&c=dXuUVkPuqc4ySEVb6ABTT9RT95x03c0ktZ5ytZVunv0=", "Toyota Corolla", "Toyota", "Corolla", "A reliable and fuel-efficient car", 50.00, 5.00));
        carList.add(new Car("2", "https://media.istockphoto.com/id/1157655660/vi/anh/suv-màu-đỏ-chung-trên-nền-trắng-chế-độ-xem-bên.jpg?s=1024x1024&w=is&k=20&c=Mrb9TS6JaFusxwFyeIz11PwHtBBYru36oO8OB948r9Q=", "Ford Mustang", "Ford", "Mustang", "A powerful and stylish sports car", 100.00, 100.0));
        carList.add(new Car("3", "https://media.istockphoto.com/id/949409516/vi/anh/hình-minh-họa-3d-của-xe-suv-chung-trên-màu-trắng.jpg?s=1024x1024&w=is&k=20&c=CB9Z0xqB6sjSIXpUy5nF5hwV49sdRdKw3BRRaO0DITU=", "Tesla Model S", "Tesla", "Model S", "A high-tech electric car with autopilot", 150.00, 15.00));
        carList.add(new Car("4", "https://media.istockphoto.com/id/1307086567/vi/anh/chung-xe-suv-hiện-đại-trong-nhà-để-xe-bê-tông.jpg?s=1024x1024&w=is&k=20&c=jFQQyvOw1NGkCm_8C0P-M3ccuPk1pecXM317eGWgWM0=", "Honda Civic", "Honda", "Civic", "A compact car with great fuel economy", 55.00, 5.50));
        carList.add(new Car("5", "https://media.istockphoto.com/id/1135255668/vi/anh/xe-hatchback-màu-xanh.jpg?s=1024x1024&w=is&k=20&c=vbA7cML3-i-xI9J69u-F1vTC_raRWrR_EtjfRNjh1xc=", "BMW 3 Series", "BMW", "3 Series", "A luxury sedan with a sporty feel", 120.00, 12.00));
        carList.add(new Car("6", "https://media.istockphoto.com/id/970028668/vi/anh/minh-họa-3d-của-mẫu-suv-màu-đỏ-generic-compact.jpg?s=1024x1024&w=is&k=20&c=Lj3IhhdkHXsXlXevVQX75EgXPFijJ1m2iL7o5bBC9z4=", "Mercedes-Benz C-Class", "Mercedes-Benz", "C-Class", "A premium car with advanced features", 130.00, 13.00));
        carList.add(new Car("7", "https://media.istockphoto.com/id/1150425295/vi/anh/minh-họa-3d-của-xe-hatchback-generic-góc-nhìn-phối-cảnh.jpg?s=1024x1024&w=is&k=20&c=X2xgD8YlcLmvQXKh5e8sv1-0BgghAHtxdzltQG1ZK48=", "Audi A4", "Audi", "A4", "A stylish and high-performance sedan", 125.00, 12.50));
        carList.add(new Car("8", "https://media.istockphoto.com/id/949483148/vi/anh/minh-họa-3d-của-xe-suv-chung-side-view.jpg?s=1024x1024&w=is&k=20&c=eqRHycdYAF1scCzU04qCgERCLwyBrJywRgn2UIiMysU=", "Chevrolet Malibu", "Chevrolet", "Malibu", "A midsize car with a comfortable ride", 60.00, 6.00));
        carList.add(new Car("9", "https://media.istockphoto.com/id/959391798/vi/anh/minh-họa-3d-của-mẫu-suv-màu-trắng-compact-chung.jpg?s=1024x1024&w=is&k=20&c=nSWgC4QbOHxlBbI-bhLi4MQGxS_EEy6QMXc3z4GjbZU=", "Nissan Altima", "Nissan", "Altima", "A reliable car with a smooth drive", 65.00, 6.50));
        carList.add(new Car("10", "https://media.istockphoto.com/id/1154617648/vi/anh/minh-họa-3d-của-xe-nhỏ-gọn-generic-front-view.jpg?s=1024x1024&w=is&k=20&c=MAeNyMQj7Vr2ytbiB0npnb05ijdenOR2Fd5HIsJZEZY=", "Hyundai Elantra", "Hyundai", "Elantra", "A compact car with modern features", 50.00, 5.00));

        return carList;
    }
}
