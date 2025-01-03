package com.example.easybooking.sampledata;

import com.example.easybooking.models.Hotel;
import java.math.BigDecimal;
import java.util.ArrayList;

public class HotelData {
    public static ArrayList<Hotel> getExampleHotelList() {
        ArrayList<Hotel> hotelList = new ArrayList<>();

        hotelList.add(new Hotel("1", "https://media.istockphoto.com/id/1418701619/vi/anh/biển-hiệu-khách-sạn-trên-mặt-tiền-tòa-nhà-trong-thành-phố-đi-công-tác-và-du-lịch.jpg?s=1024x1024&w=is&k=20&c=Hg85tASCCRHxEqDDdb4yv2_k-eNrbolx_utOGltzQ3I=",
                "Sunset Hotel", "A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views A beautiful hotel with sunset views", "Miami Beach, FL, USA", 25.790654, -80.130045, 4.5, new BigDecimal("200.00"), new BigDecimal("25.00")));

        hotelList.add(new Hotel("2", "https://media.istockphoto.com/id/1154773904/vi/anh/phòng-khách-sạn-thoải-mái.jpg?s=612x612&w=0&k=20&c=4_WqMoGFrvX6GaaCeHsRHgoyWQ1Xu-Akz8PIBx50Bfo=",
                "Mountain View Inn", "A cozy inn with mountain views", "Aspen, CO, USA", 39.191099, -106.817538, 4.7, new BigDecimal("250.00"), new BigDecimal("30.00")));

        hotelList.add(new Hotel("3", "https://media.istockphoto.com/id/627892060/vi/anh/phòng-khách-sạn-room-suite-có-tầm-nhìn.jpg?s=612x612&w=0&k=20&c=LWiHPH0bERBGWnTADDhrIVIhs5BKDY6k6uF8cemMF5o=",
                "Seaside Resort", "A luxury resort by the sea", "Malibu, CA, USA", 34.025919, -118.779757, 4.8, new BigDecimal("300.00"), new BigDecimal("35.00")));

        hotelList.add(new Hotel("4", "https://media.istockphoto.com/id/119926339/vi/anh/hồ-bơi-resort.jpg?s=1024x1024&w=is&k=20&c=p5BZ99uV8B_6mEw1h4U-iapt2AXvFmRZt84yavH0vg4=",
                "Desert Oasis", "A modern resort in the desert", "Las Vegas, NV, USA", 36.114647, -115.172813, 4.6, new BigDecimal("220.00"), new BigDecimal("27.00")));

        hotelList.add(new Hotel("5", "https://media.istockphoto.com/id/641448082/vi/anh/khu-nghỉ-dưỡng-khách-sạn-phía-trước-bãi-biển-nhiệt-đới-xinh-đẹp-với-hồ-bơi-ánh-nắng-mặt-trời.jpg?s=1024x1024&w=is&k=20&c=gPd3R7wd7R-r1avZFHDHGsbNuXiTp3J8eq2hEiv8UuM=",
                "Urban Paradise", "A chic hotel in the city center", "New York, NY, USA", 40.712776, -74.005974, 4.9, new BigDecimal("350.00"), new BigDecimal("40.00")));

        return hotelList;
    }
}
