package davs.searcher.gui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import davs.searcher.tools.SpellCheck;

public aspect SuggestionsAspect {
    
    // Since spell checking is expensive operation, hold only one copy
    private static SpellCheck spellCheck;
    private static boolean enabled;
    
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private JPanel suggestionsRow;
    
    static {
        enabled = true;

        try {
            spellCheck = new SpellCheck();
        } catch (IOException e) {
            // If spellCheck is not initialized, don't even bother with
            // suggestions
            enabled = false;

            System.err.println("Spell checking is not supported");
            System.err
                    .println("Make sure file dict is in your working directory");
        }
    }

    
    private void initSuggestionsComponents(final SearchScreen _this) {
        jLabel3.setText("Similar Words :");

        jLabel4.setText("Suggestion1");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _this.mouseClicked2(jLabel4.getText());
            }
        });

        jLabel5.setText("Suggestion2");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _this.mouseClicked2(jLabel5.getText());
            }
        });

        jLabel6.setText("Suggestion3");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _this.mouseClicked2(jLabel6.getText());
            }
        });
    }
    private JPanel buildSuggestionsRow() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(layout
                .createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                        254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                        216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE,
                        249, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(jLabel4)
                .addComponent(jLabel5)
                .addComponent(jLabel6));
        return panel;
    }    
    before(SearchScreen _this): call(private void SearchScreen.initComponents())
                         && this(_this) {
        initSuggestionsComponents(_this);
        suggestionsRow = buildSuggestionsRow();
    }
    after() returning(ParallelGroup pg):
        call(private ParallelGroup SearchScreen.buildRowsHorizontally(GroupLayout, JPanel, JPanel)) {
        pg.addComponent(suggestionsRow);
    }
    after() returning(SequentialGroup sg):
        call(private SequentialGroup SearchScreen.buildRowsVertically(GroupLayout, JPanel, JPanel)) {
        sg.addGap(18, 18, 18)
          .addComponent(suggestionsRow);
    }
    
    
    private void updateSuggestions(final SearchScreen _this, String newTerm) {
        if (!enabled) {
            return;
        }

        String[] suggestions = spellCheck.getSpellSuggestions(newTerm, 3);
        if (suggestions == null) {
            return;
        }

        jLabel4.setText("");
        jLabel5.setText("");
        jLabel6.setText("");
        
        if (suggestions.length >= 1) {
            jLabel4.setText(suggestions[0]);
            jLabel4.setForeground(Color.BLUE);
        }
        if (suggestions.length >= 2) {
            jLabel5.setText(suggestions[1]);
            jLabel5.setForeground(Color.BLUE);
        }
        if (suggestions.length >= 3) {
            jLabel6.setText(suggestions[2]);
            jLabel6.setForeground(Color.BLUE);
        }
    }
    after(SearchScreen _this, String newTerm):
        call(private void SearchScreen.mouseClicked(String))
     && this(_this)
     && args(newTerm) {
        updateSuggestions(_this, SearchScreen.leftTrim(newTerm));
    }
    
   
    
    declare warning:
        (  (get(JLabel SearchScreen.jLabel3) || set(JLabel SearchScreen.jLabel3))
        || (get(JLabel SearchScreen.jLabel4) || set(JLabel SearchScreen.jLabel4))
        || (get(JLabel SearchScreen.jLabel5) || set(JLabel SearchScreen.jLabel5))
        || (get(JLabel SearchScreen.jLabel6) || set(JLabel SearchScreen.jLabel6))
        )
        && !within(SuggestionsAspect):
        "Fields are accessed outside aspect.";

}
