/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package SourcePackages;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronor
 */
public class SellItem extends javax.swing.JFrame {

    /**
     * Creates new form SellItem
     */
    JDBC db;
    String branch, name, date, time, price, TotalPrice, instalment, item, month, RemainPrice, status, chkitem;
    public SellItem(String x, String y) {
        initComponents();
        db=new JDBC();
        branch=y;
        name=x;
        Uname.setText(x);
        Ubranch.setText(y);
        EMIPnl.setVisible(false);
        CashPnl.setVisible(false);
        EMICalcBtn.setVisible(false);
        EMIMnth.setVisible(false);
        EMIMnthLbl.setVisible(false);
        Qty.setVisible(false);
        QLbl.setVisible(false);
    }
    public SellItem() {
        initComponents();
    }
    public void cashPanel(){
        calcPrice();
        CashItem.setText((String) SelItem.getSelectedItem());
        CashMRP.setText(price);
        QtyLbl.setText(Qty.getText());
        CashTotal.setText(TotalPrice);
    }
    public void emiPanel(){
        calcPrice();
        EMIItem.setText((String) SelItem.getSelectedItem());
        EMIMrp.setText(price);
        if("3 Months".equals((String)EMIMnth.getSelectedItem())){
            EMIInterst.setText("NONE");
        }else{
            EMIInterst.setText("7.00%");
        }
        EMIMonths.setText((String)EMIMnth.getSelectedItem());
        EMITotal.setText(TotalPrice);
        EMIInstallment.setText(instalment);
    }
    public void dateTime(){
    	DateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
    	time = dateFormat.format(new Date()).toString();
    	DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
    	date = dateFormat2.format(new Date()).toString();
    }
    public void calcPrice(){
        int dbprice=Integer.valueOf(price), x, y;
        Double z=0.0;
        if("3 Months".equals((String)EMIMnth.getSelectedItem())){
            y=3;
        }else if("6 Months".equals((String)EMIMnth.getSelectedItem())){
            y=6;
        }else if("9 Months".equals((String)EMIMnth.getSelectedItem())){
            y=9;
        }else{
            y=12;
        }
        if("EMI".equals((String)mode.getSelectedItem())){
            if("3 Months".equals((String)EMIMnth.getSelectedItem())){
                x=(int) (dbprice+(dbprice*0.18));
                z=(double)x/y;
                instalment=String.valueOf(z);
            }else{
                x=(int) (dbprice+((dbprice*0.18)*0.07));
                z=(double)Math.round(x/y*100)/100;
                instalment=String.valueOf(z);
            }
        }else{
            int a=Integer.valueOf(Qty.getText());
            dbprice*=a;
            x=(int) (dbprice+(dbprice*0.18));
        }
        TotalPrice=String.valueOf(x);
        double a =(double)Math.round(x-z*100)/100;
        RemainPrice=String.valueOf(a);
        month=String.valueOf(y-1);
    }
    public void insertDbEMI(){
        try {
            dateTime();
            PreparedStatement Add = db.con.prepareStatement("insert into emi values(?,?,?,?,?,?,?,?,?,?)");
            Add.setString(1, cname.getText());
            Add.setString(2, cid.getText());
            Add.setString(3, TotalPrice);
            Add.setString(4, instalment);
            Add.setString(5, month);
            Add.setString(6, "1");
            Add.setString(7, RemainPrice);
            Add.setString(8, "Running");
            Add.setString(9, (String)SelItem.getSelectedItem());
            Add.setString(10, branch);

            Add.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "imei");
        }
    }
    public void stockReduce(){
        try {
                String Query = "select * from stock where branch='"+branch.toLowerCase()+"'";
                String x="",y=(String)SelItem.getSelectedItem(), Query2="";
                db.st = db.con.createStatement();
                db.rs = db.st.executeQuery(Query);
                if(db.rs.next()){
                    x=db.rs.getString((String)SelItem.getSelectedItem());
                }
                int a=Integer.valueOf(x);
                int b=Integer.valueOf(Qty.getText());
                if("Car".equals(y)){
                    Query2="update stock set car=? where branch=?";
                }else if("Bike".equals(y)){
                    Query2="update stock set bike=? where branch=?";
                }else if("Laptop".equals(y)){
                    Query2="update stock set laptop=? where branch=?";
                }else if("Cycle".equals(y)){
                    Query2="update stock set cycle=? where branch=?";
                }else if("Inverter".equals(y)){
                    Query2="update stock set inverter=? where branch=?";
                }else if("AC".equals(y)){
                    Query2="update stock set ac=? where branch=?";
                }else if("TV".equals(y)){
                    Query2="update stock set tv=? where branch=?";
                }else{
                    Query2="update stock set fridge=? where branch=?";
                }
                    
                PreparedStatement Add = db.con.prepareStatement(Query2);
                Add.setString(1, String.valueOf(a-b));
                Add.setString(2, branch.toLowerCase());
                Add.executeUpdate();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
    }
    public void check(){
        try {
            String Query = "select * from emi where eid='"+cid.getText()+"'";
            db.st = db.con.createStatement();
            db.rs = db.st.executeQuery(Query);
            if(db.rs.next()){
                status=db.rs.getString("estatus");
                chkitem=db.rs.getString("eitem");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    public void insertDb(){
        try {
            dateTime();
            PreparedStatement Add = db.con.prepareStatement("insert into payment values(?,?,?,?,?,?,?,?,?)");
            Add.setString(1, cname.getText());
            Add.setString(2, cid.getText());
            Add.setString(3, (String)SelItem.getSelectedItem());
            Add.setString(4, (String)mode.getSelectedItem());
            Add.setString(5, date);
            Add.setString(6, time);
            Add.setString(7, TotalPrice);
            Add.setString(8, branch);
            Add.setString(9, Qty.getText());

            Add.executeUpdate();
            
            PreparedStatement Add2 = db.con.prepareStatement("insert into statement values(?,?,?,?,?,?,?,?,?)");
            Add2.setString(1, (String)SelItem.getSelectedItem());
            Add2.setString(2, cname.getText());
            Add2.setString(3, date);
            Add2.setString(4, time);
            Add2.setString(5, (String)mode.getSelectedItem());
            Add2.setString(6, cid.getText());
            Add2.setString(7, branch);
            Add2.setString(8, TotalPrice);
            Add2.setString(9, Qty.getText());

            Add2.executeUpdate();
            stockReduce();
            JOptionPane.showMessageDialog(this, "Payment Done.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "idb");
        }
    }
    
    public void mrp(){
        try {//'"+item.toLowerCase()+"'
            String Query = "select * from stock where branch='price'";
            db.st = db.con.createStatement();
            db.rs = db.st.executeQuery(Query);
            if(db.rs.next()){
                price=db.rs.getString(item.toLowerCase());
            }else{
                JOptionPane.showMessageDialog(this, "Price Not Fetched!!!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "imrp");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        user = new javax.swing.JLabel();
        user1 = new javax.swing.JLabel();
        Ubranch = new javax.swing.JLabel();
        Uname = new javax.swing.JLabel();
        bike1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        SelItem = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cid = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        mode = new javax.swing.JComboBox<>();
        EMIMnthLbl = new javax.swing.JLabel();
        EMIMnth = new javax.swing.JComboBox<>();
        bike2 = new javax.swing.JButton();
        EMICalcBtn = new javax.swing.JButton();
        EMIPnl = new javax.swing.JPanel();
        user2 = new javax.swing.JLabel();
        EMIMrp = new javax.swing.JLabel();
        user4 = new javax.swing.JLabel();
        user5 = new javax.swing.JLabel();
        user6 = new javax.swing.JLabel();
        user7 = new javax.swing.JLabel();
        EMIMonths = new javax.swing.JLabel();
        user9 = new javax.swing.JLabel();
        EMIInterst = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        user11 = new javax.swing.JLabel();
        EMITotal = new javax.swing.JLabel();
        user13 = new javax.swing.JLabel();
        EMIInstallment = new javax.swing.JLabel();
        EMIPayment = new javax.swing.JButton();
        EMIItem = new javax.swing.JLabel();
        CashPnl = new javax.swing.JPanel();
        user15 = new javax.swing.JLabel();
        user16 = new javax.swing.JLabel();
        CashMRP = new javax.swing.JLabel();
        user20 = new javax.swing.JLabel();
        user21 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        CashTotal = new javax.swing.JLabel();
        user22 = new javax.swing.JLabel();
        CashPayment = new javax.swing.JButton();
        CashItem = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        QtyLbl = new javax.swing.JLabel();
        QLbl = new javax.swing.JLabel();
        Qty = new javax.swing.JTextField();
        cphno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user.setForeground(new java.awt.Color(255, 255, 255));
        user.setText("User : ");
        jPanel5.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 134, 62, 18));

        user1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        user1.setForeground(new java.awt.Color(255, 255, 255));
        user1.setText("Branch : ");
        jPanel5.add(user1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 163, -1, -1));

        Ubranch.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        Ubranch.setForeground(new java.awt.Color(255, 255, 255));
        Ubranch.setText("Name...");
        jPanel5.add(Ubranch, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 163, 129, -1));

        Uname.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        Uname.setForeground(new java.awt.Color(255, 255, 255));
        Uname.setText("Name...");
        jPanel5.add(Uname, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 134, 116, 23));

        bike1.setBackground(new java.awt.Color(1, 1, 1));
        bike1.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        bike1.setForeground(new java.awt.Color(254, 247, 247));
        bike1.setText("Back");
        bike1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bike1ActionPerformed(evt);
            }
        });
        jPanel5.add(bike1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, -1, -1));

        jPanel1.setBackground(new java.awt.Color(66, 95, 235));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Item :");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 84, -1));

        SelItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cycle", "Bike", "Car", "Laptop", "Inverter", "AC", "TV", "Fridge", " " }));
        jPanel1.add(SelItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 20, 120, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Name :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 47, 84, 30));
        jPanel1.add(cname, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 147, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Aadhar No :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, 20));

        cid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cidActionPerformed(evt);
            }
        });
        jPanel1.add(cid, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 147, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Payment :");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 79, -1));

        mode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "EMI" }));
        jPanel1.add(mode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 90, -1));

        EMIMnthLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EMIMnthLbl.setText("Months :");
        jPanel1.add(EMIMnthLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 73, -1));

        EMIMnth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3 Months", "6 Months", "9 Months", "12 Months" }));
        EMIMnth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EMIMnthActionPerformed(evt);
            }
        });
        jPanel1.add(EMIMnth, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 130, -1));

        bike2.setBackground(new java.awt.Color(128, 12, 112));
        bike2.setFont(new java.awt.Font("Tahoma", 3, 10)); // NOI18N
        bike2.setForeground(new java.awt.Color(255, 255, 255));
        bike2.setText("Set");
        bike2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bike2ActionPerformed(evt);
            }
        });
        jPanel1.add(bike2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 60, -1));

        EMICalcBtn.setBackground(new java.awt.Color(1, 1, 1));
        EMICalcBtn.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        EMICalcBtn.setForeground(new java.awt.Color(254, 247, 247));
        EMICalcBtn.setText("Calculate");
        EMICalcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EMICalcBtnActionPerformed(evt);
            }
        });
        jPanel1.add(EMICalcBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, 26));

        EMIPnl.setBackground(new java.awt.Color(254, 254, 254));
        EMIPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user2.setForeground(new java.awt.Color(1, 1, 1));
        user2.setText("Item :");
        EMIPnl.add(user2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 50, -1));

        EMIMrp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMIMrp.setForeground(new java.awt.Color(1, 1, 1));
        EMIMrp.setText("............");
        EMIPnl.add(EMIMrp, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 36, -1, -1));

        user4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user4.setForeground(new java.awt.Color(1, 1, 1));
        user4.setText("MRP :");
        EMIPnl.add(user4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 34, -1, -1));

        user5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user5.setForeground(new java.awt.Color(1, 1, 1));
        user5.setText("Tax :");
        EMIPnl.add(user5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        user6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user6.setForeground(new java.awt.Color(1, 1, 1));
        user6.setText("18%");
        EMIPnl.add(user6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        user7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user7.setForeground(new java.awt.Color(1, 1, 1));
        user7.setText("Months :");
        EMIPnl.add(user7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 138, -1, -1));

        EMIMonths.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMIMonths.setForeground(new java.awt.Color(1, 1, 1));
        EMIMonths.setText("............");
        EMIPnl.add(EMIMonths, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 140, -1, -1));

        user9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user9.setForeground(new java.awt.Color(1, 1, 1));
        user9.setText("Interest :");
        EMIPnl.add(user9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 57, -1, -1));

        EMIInterst.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMIInterst.setForeground(new java.awt.Color(1, 1, 1));
        EMIInterst.setText("............");
        EMIPnl.add(EMIInterst, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 59, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(1, 1, 1));
        jLabel9.setText("__________________________");
        EMIPnl.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 160, -1));

        user11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user11.setForeground(new java.awt.Color(1, 1, 1));
        user11.setText("Total :");
        EMIPnl.add(user11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, -1, -1));

        EMITotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMITotal.setForeground(new java.awt.Color(1, 1, 1));
        EMITotal.setText("...............");
        EMIPnl.add(EMITotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 117, 120, -1));

        user13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user13.setForeground(new java.awt.Color(1, 1, 1));
        user13.setText("EMI : ");
        EMIPnl.add(user13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 161, -1, -1));

        EMIInstallment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMIInstallment.setForeground(new java.awt.Color(1, 1, 1));
        EMIInstallment.setText("............");
        EMIPnl.add(EMIInstallment, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 100, 20));

        EMIPayment.setBackground(new java.awt.Color(1, 1, 1));
        EMIPayment.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        EMIPayment.setForeground(new java.awt.Color(254, 247, 247));
        EMIPayment.setText("Proceed");
        EMIPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EMIPaymentActionPerformed(evt);
            }
        });
        EMIPnl.add(EMIPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        EMIItem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        EMIItem.setForeground(new java.awt.Color(1, 1, 1));
        EMIItem.setText(".........");
        EMIPnl.add(EMIItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 14, 70, 20));

        jPanel1.add(EMIPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 150, 240));

        CashPnl.setBackground(new java.awt.Color(254, 254, 254));
        CashPnl.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user15.setForeground(new java.awt.Color(1, 1, 1));
        user15.setText("Item :");
        CashPnl.add(user15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 50, -1));

        user16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user16.setForeground(new java.awt.Color(1, 1, 1));
        user16.setText("MRP :");
        CashPnl.add(user16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 34, -1, -1));

        CashMRP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CashMRP.setForeground(new java.awt.Color(1, 1, 1));
        CashMRP.setText("............");
        CashPnl.add(CashMRP, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 36, 70, -1));

        user20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user20.setForeground(new java.awt.Color(1, 1, 1));
        user20.setText("18%");
        CashPnl.add(user20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 59, -1, -1));

        user21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user21.setForeground(new java.awt.Color(1, 1, 1));
        user21.setText("Tax :");
        CashPnl.add(user21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 57, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(1, 1, 1));
        jLabel10.setText("_____________________");
        CashPnl.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, -1, -1));

        CashTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CashTotal.setForeground(new java.awt.Color(1, 1, 1));
        CashTotal.setText("...............");
        CashPnl.add(CashTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 135, 80, 20));

        user22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        user22.setForeground(new java.awt.Color(1, 1, 1));
        user22.setText("Total :");
        CashPnl.add(user22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        CashPayment.setBackground(new java.awt.Color(1, 1, 1));
        CashPayment.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        CashPayment.setForeground(new java.awt.Color(254, 247, 247));
        CashPayment.setText("Proceed");
        CashPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashPaymentActionPerformed(evt);
            }
        });
        CashPnl.add(CashPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        CashItem.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CashItem.setForeground(new java.awt.Color(1, 1, 1));
        CashItem.setText("............");
        CashPnl.add(CashItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 14, 70, 20));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(1, 1, 1));
        jLabel11.setText("Quantity : ");
        CashPnl.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        QtyLbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        QtyLbl.setForeground(new java.awt.Color(1, 1, 1));
        QtyLbl.setText(".......");
        CashPnl.add(QtyLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 70, -1));

        jPanel1.add(CashPnl, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 130, 240));

        QLbl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        QLbl.setText("Quantity :");
        jPanel1.add(QLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 80, -1));

        Qty.setText("1");
        Qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QtyActionPerformed(evt);
            }
        });
        jPanel1.add(Qty, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 90, -1));

        cphno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cphnoActionPerformed(evt);
            }
        });
        jPanel1.add(cphno, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 147, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Phone No :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, 20));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 186, 580, 290));

        jPanel16.setBackground(new java.awt.Color(66, 99, 235));

        jButton14.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton14.setText("X");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14jButton7ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Rockwell Extra Bold", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Engineers Microfinance Services");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-bank-100.png"))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addContainerGap(51, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton14)
                        .addGap(22, 22, 22))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addGap(165, 165, 165))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(135, 135, 135))))
        );

        jPanel5.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 130));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(600, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bike1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bike1ActionPerformed
        new MainMenu(name,branch).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_bike1ActionPerformed

    private void bike2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bike2ActionPerformed
        if(cname.getText().isEmpty() && cid.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Enter Name & Aadhar.");
        }else{
            item=(String)SelItem.getSelectedItem();
            mrp();
            check();
            if(chkitem==null){
                chkitem="";
            }
            if(chkitem.equals((String)SelItem.getSelectedItem()) && status.equals("Running") && "EMI".equals((String)mode.getSelectedItem())){
                JOptionPane.showMessageDialog(this, "You have already purchased this item through EMI, to purchase again please pay your EMI or buy with cash.");
            }
            else if("EMI".equals((String)mode.getSelectedItem())){
                EMICalcBtn.setVisible(true);
                EMIMnth.setVisible(true);
                EMIMnthLbl.setVisible(true);
                CashPnl.setVisible(false);
                Qty.setVisible(false);
                QLbl.setVisible(false);

            }else{
                EMICalcBtn.setVisible(false);
                EMIMnth.setVisible(false);
                EMIMnthLbl.setVisible(false);
                EMIPnl.setVisible(false);
                Qty.setVisible(true);
                QLbl.setVisible(true);
                EMICalcBtn.setVisible(true);
            }
        }
        
    }//GEN-LAST:event_bike2ActionPerformed

    private void cidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cidActionPerformed

    private void EMICalcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EMICalcBtnActionPerformed
        if(Qty.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Price Not Fetched!!!");
        }else{
            if("EMI".equals((String)mode.getSelectedItem())){
                item=(String)SelItem.getSelectedItem();
                mrp();
                emiPanel();
                EMIPnl.setVisible(true);
            }else{
                item=(String)SelItem.getSelectedItem();
                mrp();
                cashPanel();
                CashPnl.setVisible(true);
            }
        }
        
    }//GEN-LAST:event_EMICalcBtnActionPerformed

    private void EMIMnthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EMIMnthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EMIMnthActionPerformed

    private void CashPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashPaymentActionPerformed
        insertDb();
        CashPayment.setVisible(false);
        String [] detail = {cname.getText(),cid.getText(),cphno.getText(),date,CashItem.getText(),CashMRP.getText(),QtyLbl.getText(),CashTotal.getText()};
        try {
            new PrintPDF(name,branch,detail,"Cash").setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SellItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_CashPaymentActionPerformed

    private void EMIPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EMIPaymentActionPerformed
        insertDb();
        insertDbEMI();
        EMIPayment.setVisible(false);
        String [] detail = {cname.getText(),cid.getText(),cphno.getText(),date,EMIItem.getText(),EMIMrp.getText(),"1",EMITotal.getText(),EMIInterst.getText(),EMIMonths.getText(),EMIInstallment.getText()};
        try {
            new PrintPDF(name,branch,detail,"EMI").setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SellItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_EMIPaymentActionPerformed

    private void QtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_QtyActionPerformed

    private void jButton14jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14jButton7ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton14jButton7ActionPerformed

    private void cphnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cphnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cphnoActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CashItem;
    private javax.swing.JLabel CashMRP;
    private javax.swing.JButton CashPayment;
    private javax.swing.JPanel CashPnl;
    private javax.swing.JLabel CashTotal;
    private javax.swing.JButton EMICalcBtn;
    private javax.swing.JLabel EMIInstallment;
    private javax.swing.JLabel EMIInterst;
    private javax.swing.JLabel EMIItem;
    private javax.swing.JComboBox<String> EMIMnth;
    private javax.swing.JLabel EMIMnthLbl;
    private javax.swing.JLabel EMIMonths;
    private javax.swing.JLabel EMIMrp;
    private javax.swing.JButton EMIPayment;
    private javax.swing.JPanel EMIPnl;
    private javax.swing.JLabel EMITotal;
    private javax.swing.JLabel QLbl;
    private javax.swing.JTextField Qty;
    private javax.swing.JLabel QtyLbl;
    private javax.swing.JComboBox<String> SelItem;
    private javax.swing.JLabel Ubranch;
    private javax.swing.JLabel Uname;
    private javax.swing.JButton bike1;
    private javax.swing.JButton bike2;
    private javax.swing.JTextField cid;
    private javax.swing.JTextField cname;
    private javax.swing.JTextField cphno;
    private javax.swing.JButton jButton14;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JComboBox<String> mode;
    private javax.swing.JLabel user;
    private javax.swing.JLabel user1;
    private javax.swing.JLabel user11;
    private javax.swing.JLabel user13;
    private javax.swing.JLabel user15;
    private javax.swing.JLabel user16;
    private javax.swing.JLabel user2;
    private javax.swing.JLabel user20;
    private javax.swing.JLabel user21;
    private javax.swing.JLabel user22;
    private javax.swing.JLabel user4;
    private javax.swing.JLabel user5;
    private javax.swing.JLabel user6;
    private javax.swing.JLabel user7;
    private javax.swing.JLabel user9;
    // End of variables declaration//GEN-END:variables
}
