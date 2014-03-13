package davs.searcher.gui;

import java.awt.Color;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import davs.searcher.program.MainProgram;

public aspect SuggestionsAspect {
    
    private JLabel SearchScreen.jLabel3 = new JLabel();
    private JLabel SearchScreen.jLabel4 = new JLabel();
    private JLabel SearchScreen.jLabel5 = new JLabel();
    private JLabel SearchScreen.jLabel6 = new JLabel();

    
    void SearchScreen.initSuggestionsComponents() {
        jLabel3.setText("Similar Words :");

        jLabel4.setText("Suggestion1");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicked2(jLabel4.getText());
            }
        });

        jLabel5.setText("Suggestion2");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicked2(jLabel5.getText());
            }
        });

        jLabel6.setText("Suggestion3");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicked2(jLabel6.getText());
            }
        });
    }
    
    before(SearchScreen _this): call(private void SearchScreen.initComponents())
                         && this(_this) {
        _this.initSuggestionsComponents();
    }
    
    
    void SearchScreen.updateSuggestions(String newTerm) {
        // spell check
        if (MainProgram.isSpellCheckEnabled)
            suggestions = MainProgram.spellCheck
                    .getSpellSuggestions(newTerm, 3);

        if (suggestions != null && suggestions.length >= 1
                && MainProgram.isSpellCheckEnabled) {
            jLabel4.setText(suggestions[0]);
            jLabel4.setForeground(Color.BLUE);
        } else
            jLabel4.setText("");

        if (suggestions != null && suggestions.length >= 2
                && MainProgram.isSpellCheckEnabled) {
            jLabel5.setText(suggestions[1]);
            jLabel5.setForeground(Color.BLUE);
        } else
            jLabel5.setText("");

        if (suggestions != null && suggestions.length >= 3
                && MainProgram.isSpellCheckEnabled) {
            jLabel6.setText(suggestions[2]);
            jLabel6.setForeground(Color.BLUE);
        } else
            jLabel6.setText("");
        // end spell check
    }
    after(SearchScreen _this, String newTerm):
        call(private void SearchScreen.mouseClicked(String))
     && this(_this)
     && args(newTerm) {
        _this.updateSuggestions(SearchScreen.leftTrim(newTerm));
    }
    
    private JPanel suggestionsRow;
    JPanel SearchScreen.buildSuggestionsRow() {
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
        suggestionsRow = _this.buildSuggestionsRow();
    }
    after(SearchScreen _this) returning(ParallelGroup pg):
        call(private ParallelGroup SearchScreen.buildRowsHorizontally(GroupLayout, JPanel, JPanel))
     && this(_this) {
        pg.addComponent(suggestionsRow);
    }
    after(SearchScreen _this) returning(SequentialGroup sg):
        call(private SequentialGroup SearchScreen.buildRowsVertically(GroupLayout, JPanel, JPanel))
     && this(_this) {
        sg.addGap(18, 18, 18)
          .addComponent(suggestionsRow);
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
