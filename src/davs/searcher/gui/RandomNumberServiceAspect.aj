package davs.searcher.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.Timer;

public aspect RandomNumberServiceAspect {
    
    private static java.util.Random random = new java.util.Random();
    private static javax.swing.JLabel jLabel = new javax.swing.JLabel();

 
    before(SearchScreen _this): call(private void SearchScreen.initComponents())
    && this(_this) {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                jLabel.setText("Random Number Service presents: " + random.nextInt());
            }
        };
        Timer displayTimer = new Timer(5000, listener);
        displayTimer.start();
    }
    
    after() returning (ParallelGroup pg): call(ParallelGroup buildRowsHorizontally(..)) {
        pg.addComponent(jLabel);
    }
    after() returning (SequentialGroup sg): call(SequentialGroup buildRowsVertically(..)) {
        sg.addGap(18, 18, 18)
          .addComponent(jLabel);
    }

}
