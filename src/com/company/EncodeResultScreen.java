package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Image;

public class EncodeResultScreen extends JFrame {
    // default width and height for img
    private int width = 500;
    private int height = 500;
    private ImageIcon imageIcon;
    private JLabel img;

    public EncodeResultScreen(String title) {
        super(title);

        // set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // default font
        Font font = new Font("Times New Roman",Font.PLAIN,50);

        // swing components
        // title
        JLabel encodedResultLbl = new JLabel("Encoded Result");
        encodedResultLbl.setFont(font);

        // image
        String imageName;
        if(EncodeScreen.isEncImg) {
            imageName = "encodedImg-" + EncodeScreen.fileName;
        } else {
            imageName = "encodedMsg-" + EncodeScreen.fileName;
        }

        String imagePath = EncodeScreen.filePath + "/" + imageName;

        imageIcon = new ImageIcon(imagePath);
        Image image = imageIcon.getImage(); // transform it
        Image newImg = image.getScaledInstance(width, height,  Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newImg);  // transform it back
        img = new JLabel(imageIcon);

        final Dimension size = new Dimension(500,500);
        img.setMinimumSize(size);
        img.setPreferredSize(size);
        img.setMaximumSize(size);

        // buttons
        JButton save = new JButton("SAVE");
        JButton home = new JButton("HOME");
        JButton encodeAnother = new JButton("ENCODE ANOTHER");
        JButton decode = new JButton("DECODE");

        // Zoom in/out
        JButton zoomIn = new JButton("Zoom In");
        JButton zoomOut = new JButton("Zoom Out");

        //// Add components to frame //////////////////////////////////////////////////
        gc.weightx = 0;
        gc.weighty = 0.25;

        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 1;
        gc.gridy = 0;
        add(encodedResultLbl, gc);

        gc.anchor = GridBagConstraints.PAGE_START;
        gc.gridx = 1;
        gc.gridy = 1;
        add(img, gc);

        gc.ipadx = 25;
        gc.ipady = 15;
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.gridx = 1;
        gc.gridy = 1;
        add(zoomIn, gc);

        gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.gridx = 1;
        gc.gridy = 1;
        add(zoomOut, gc);

        gc.ipadx = 50;
        gc.ipady = 25;
        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 0;
        gc.gridy = 2;
        add(save, gc);

        gc.anchor = GridBagConstraints.LINE_START;
        gc.gridx = 1;
        add(home, gc);

        gc.anchor = GridBagConstraints.LINE_END;
        gc.gridx = 1;
        add(encodeAnother, gc);

        gc.anchor = GridBagConstraints.CENTER;
        gc.gridx = 2;
        add(decode, gc);

        //// Add Behaviors ///////////////////////////////////
        // zoom in pressed
        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Zoom in pressed.");

                // remove existing img
                remove(img);

                if(width >= 1000 || height >= 1000) {
                    Log.LOGGER.info("ERROR: Max zoom for image reached.");
                    return;
                } else {
                    // adjust height and width
                    height += 50;
                    width += 50;
                    Log.LOGGER.info("Zooming in image by 50.");
                }

                // rescale img
                imageIcon = new ImageIcon(image.getScaledInstance(width, height,  Image.SCALE_SMOOTH));

                // create new jlabel to store img
                img = new JLabel(imageIcon);
                img.setMinimumSize(size);
                img.setPreferredSize(size);
                img.setMaximumSize(size);

                // add img to frame and revalidate
                gc.anchor = GridBagConstraints.PAGE_START;
                gc.gridx = 1;
                gc.gridy = 1;
                add(img, gc);
                revalidate();
                Log.LOGGER.info("Revalidated screen.");
            }
        });

        // zoom out pressed
        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Zoom out pressed.");
                // if max zoom out reached, don't zoom out
                if(width <= 500 || height <= 500) {
                    Log.LOGGER.info("ERROR: Minimum zoom reached.");
                    return;
                } else {
                    // adjust height and width
                    height -= 50;
                    width -= 50;
                    Log.LOGGER.info("Zooming out of image by 50.");
                }

                // remove existing img
                remove(img);

                // rescale img
                imageIcon = new ImageIcon(image.getScaledInstance(width, height,  Image.SCALE_SMOOTH));

                // create new jlabel to store img
                img = new JLabel(imageIcon);
                img.setMinimumSize(size);
                img.setPreferredSize(size);
                img.setMaximumSize(size);

                // add img to frame and revalidate
                gc.anchor = GridBagConstraints.PAGE_START;
                gc.gridx = 1;
                gc.gridy = 1;
                add(img, gc);
                revalidate();
                Log.LOGGER.info("Revalidated screen.");
            }
        });

        // save button pressed
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Save button pressed.");
                // create img to save
                Image img = imageIcon.getImage();

                BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_4BYTE_ABGR);

                Graphics2D g2 = bi.createGraphics();
                g2.drawImage(img, 0, 0, null);
                g2.dispose();

                //Creating the save option
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
                if(chooser.showSaveDialog(null)== JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();

                    try{
                        ImageIO.write(bi, "png", new File(file.getAbsolutePath()));
                        Log.LOGGER.info("Saved image as png.");
                    } catch (IOException ex){
                        Log.LOGGER.info("ERROR: Failed to save image.");
                    }
                } else {
                    Log.LOGGER.info("No file selected.");
                }
                String fileLoc = chooser.getSelectedFile().getAbsolutePath();       //The Location of the file
                Log.LOGGER.info("Location of file: " + fileLoc);
            }
        });

        // home button pressed
        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Home button pressed.");
                // open welcome screen
                JFrame welcome = new WelcomeFrame("ImageEncoder");
                welcome.setResizable(false);
                welcome.setExtendedState(JFrame.MAXIMIZED_BOTH);
                welcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                welcome.setVisible(true);
                Log.LOGGER.info("Opened welcome screen.");

                // close encoded result screen
                setVisible(false);
                dispose();
                Log.LOGGER.info("Disposed of the encoded result screen/");
            }
        });

        // encode another button pressed
        encodeAnother.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Encode Another button pressed.");
                // open encode screen
                JFrame encodeScreen = new EncodeScreen("Encode");
                encodeScreen.setResizable(false);
                encodeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
                encodeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                encodeScreen.setVisible(true);
                Log.LOGGER.info("Encode screen opened.");

                // close encoded result screen
                setVisible(false);
                dispose();
                Log.LOGGER.info("Disposed of the encoded result screen.");
            }
        });

        // decode button pressed
        decode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Log.LOGGER.info("Decode button pressed.");
                // open decode screen
                JFrame decodeScreen = new DecodeScreen("Decode");
                decodeScreen.setResizable(false);
                decodeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
                decodeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                decodeScreen.setVisible(true);
                Log.LOGGER.info("Decode screen opened.");

                // close encoded result screen
                setVisible(false);
                dispose();
                Log.LOGGER.info("Disposed of the encoded result screen.");
            }
        });
    }

}
