package davs.searcher.gui;

import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;

public aspect Debug {
    
    private static final LineBorder redBorder = new LineBorder(Color.red);
    private static final LineBorder greenBorder = new LineBorder(Color.green, 2);

    after(JComponent c): set(private JComponent+ SearchScreen.*) && args(c) {
        String previousTooltip = c.getToolTipText();
        if (previousTooltip == null) {
            previousTooltip = "";
        }
        c.setToolTipText(String.format("[%s]%s",
                                       thisJoinPoint.getSignature().getName(),
                                       previousTooltip));
        c.setBorder(redBorder);
    }
    
    after() returning (JComponent c): call(JComponent+.new(..)) {
        c.setBorder(greenBorder);
    }
}
