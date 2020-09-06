package sample;

import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class Controller {
    @FXML
    TextField disk_count;
    @FXML
    Button submit_btn;
    @FXML
    Label error_lb;
    @FXML
    AnchorPane pane;
    ArrayList<Rectangle> list = new ArrayList<>();
    ArrayList<Animation> transitions = new ArrayList<>();
    int counter_A = 0;
    int counter_B = 0;
    int counter_C = 0;

    public void submit_clicked(MouseEvent mouseEvent) {
        try {
            int n = (Integer.parseInt(disk_count.getText()));
            if (n > 0 && n < 9) {
                disk_count.setVisible(false);
                submit_btn.setVisible(false);
                error_lb.setVisible(false);
                ArrayList<Disk> disks = create_disks(n);
                add_disk(disks);
                counter_A = n;
                towerOfHanoi(n, 'A', 'B', 'C');
                SequentialTransition transition = new SequentialTransition();
                transition.getChildren().addAll(transitions);
                transition.play();
            } else {
                error_lb.setVisible(true);
            }
        } catch (Exception e) {
            error_lb.setVisible(true);
        }
    }


    public ArrayList<Disk> create_disks(int n) {

        final int diff = 30;
        int curr_width = 60;
        ArrayList<Disk> disks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            disks.add(new Disk(curr_width));
            curr_width += diff;

        }

        Collections.reverse(disks);

        return disks;
    }

    public void add_disk(ArrayList<Disk> disks) {
        int posY = Helper.page_end;
        int posX = 16 * disks.size();
        for (Disk disk : disks) {
            Rectangle rect = new Rectangle(Helper.rod_A.x - posX, posY, disk.width, disk.height);
            rect.setFill(Color.DARKORANGE);
            rect.setStroke(Color.BLACK);
            pane.getChildren().add(rect);
            list.add(rect);
            posY -= Helper.height;
            posX -= 15;
        }

        Collections.reverse(list);
    }


    void towerOfHanoi(int n, char from_rod, char to_rod, char aux_rod) {
        if (n == 1) {
            System.out.println("Move disk 1 from rod " + from_rod + " to rod " + to_rod);

            TranslateTransition translate = new TranslateTransition();
            translate.setByX(getX_transition(from_rod, to_rod));
            translate.setByY(getY_transition(from_rod, to_rod, n));
            translate.setCycleCount(1);
            translate.setNode(list.get(n - 1));
            translate.setDuration(Duration.millis(1000));
            transitions.add(translate);
            return;
        }

        towerOfHanoi(n - 1, from_rod, aux_rod, to_rod);
        System.out.println("Move disk " + n + " from rod " + from_rod + " to rod " + to_rod);

        TranslateTransition translate = new TranslateTransition();
        translate.setByX(getX_transition(from_rod, to_rod));
        translate.setByY(getY_transition(from_rod, to_rod, n));
        translate.setCycleCount(1);
        translate.setNode(list.get(n - 1));
        translate.setDuration(Duration.millis(1000));
        transitions.add(translate);

        towerOfHanoi(n - 1, aux_rod, to_rod, from_rod);

    }

    private int getY_transition(char from_rod, char to_rod, int n) {
        int from_rod_counter = find_from_rod_counter(from_rod);
        int rod_counter = find_to_rod_counter(to_rod);
        return from_rod_counter * Helper.height - rod_counter * Helper.height;
    }


    int find_to_rod_counter(char to_rod) {
        int rod_counter = 0;
        switch (to_rod) {
            case 'A':
                counter_A++;
                rod_counter = counter_A;
                break;
            case 'B':
                counter_B++;
                rod_counter = counter_B;
                break;
            case 'C':
                counter_C++;
                rod_counter = counter_C;
                break;
        }

        return rod_counter;
    }


    int find_from_rod_counter(char from_rod) {
        int rod_counter = 0;
        switch (from_rod) {
            case 'A':
                rod_counter = counter_A;
                counter_A--;
                break;
            case 'B':
                rod_counter = counter_B;
                counter_B--;
                break;
            case 'C':
                rod_counter = counter_C;
                counter_C--;
                break;
        }

        return rod_counter;
    }


    int getX_transition(char from_rod, char to_rod) {
        int distance = 0;
        switch (from_rod - to_rod) {
            case -2:
                distance = Helper.rod_distance * 2;
                break;
            case -1:
                distance = Helper.rod_distance;
                break;
            case 1:
                distance = -Helper.rod_distance;
                break;
            case 2:
                distance = -Helper.rod_distance * 2;

                break;
        }
        return distance;
    }


}
