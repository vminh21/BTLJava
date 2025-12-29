/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BTL.bussiness;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author vanminh
 */
public interface Dao<T> {
    List<T> getall();
    Optional<T> get(int id);
    int insert(T t);
    boolean update(T t);
    boolean delete(T t);
    boolean delete(int id);
}
