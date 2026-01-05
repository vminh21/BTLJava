/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.entity;

/**
 *
 * @author macbook
 */
/**
 * Class hỗ trợ ComboBox: Hiển thị tên nhưng lấy giá trị là ID
 */
public class ComboItem {
    private int id;
    private String name;

    public ComboItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Hàm này quyết định cái gì sẽ hiện lên trên ComboBox
    @Override
    public String toString() {
        return name;
    }
}