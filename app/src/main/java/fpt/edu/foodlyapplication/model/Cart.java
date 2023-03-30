package fpt.edu.foodlyapplication.model;

public class Cart {
    /**
     @param id mã định danh duy nhất của mặt hàng trong giỏ hàng
     @param name sản phẩm trong giỏ hàng
     @param image hình ảnh của sản phẩm trong giỏ hàng
     @param quantity lượng số lượng sản phẩm trong giỏ hàng
     @param price giá của một đơn vị sản phẩm trong giỏ hàng
     @param sumPrice tổng giá của sản phẩm trong giỏ hàng
     */
    private int id;
    private String name;
    private String image;
    private int quantity;
    private int price;
    private int sumPrice;

    public Cart() {
    }

    public Cart(int id, String name, String image, int quantity, int price, int sumPrice) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.sumPrice = sumPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(int sumPrice) {
        this.sumPrice = sumPrice;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", sumPrice=" + sumPrice +
                '}';
    }
}
