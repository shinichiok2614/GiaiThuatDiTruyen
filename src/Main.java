import nguoibanhang.NguoiBanHang;

public class Main {
    public static void main(String[] args) {
        NguoiBanHang nguoiBanHang=new NguoiBanHang();
        nguoiBanHang.khoitao();
        for (int i = 0; i < 100; i++) {
            nguoiBanHang.danhgia();
            nguoiBanHang.print();
            nguoiBanHang.chonloc();
            nguoiBanHang.laighep();
            nguoiBanHang.dotbien();
        }
    }
}