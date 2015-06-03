package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import javax.swing.*;

import model.Player;
import model.Ponto;
import model.Territorio;

@SuppressWarnings("serial")
public class MapPanel extends JPanel {

    // Lista de territorios
    private List<Territorio> lstTerritorios = new ArrayList<>();
    
    // deslocaX e deslocaY � utilizado para alinhar os poligonos criados em cima da imagem dos territorios.
    private final float deslocaX = -5;
    private final float deslocaY = -9.6f;
    private BufferedImage mapImage;
    private BufferedImage lineImage;
    private BufferedImage infosImage;
    private List<Color> playerColors;
    private Player currentPlayer;

    public MapPanel(List<Player> playerList) {
        try{
            File map = new File("images/mapas/war_tabuleiro_fundo.png");
            mapImage = ImageIO.read(map);
            
            File line = new File("images/mapas/war_tabuleiro_linhas.png");
            lineImage = ImageIO.read(line);
            
            File infos = new File("images/mapas/war_tabuleiro.png");
            infosImage = ImageIO.read(infos);
            
            playerColors = new ArrayList<>();
            playerList.forEach((Player) -> playerColors.add(Player.getColor()));
        } 
        catch(IOException e){
            System.out.print("Erro ao carregar a imagem" + e.getMessage());
        }   

        this.addMouseListener(new MouseListener() {

            // Evento de click para detectar se o ponto clicado esta dentro da area do teritorio.
            @Override
            public void mouseClicked(MouseEvent e) {
                // Para cada territorio da lista de territorios
                for(Territorio t : lstTerritorios) {

                    // Se o ponto clicado for contido pelo poligono do territorio	
                    if(t.getPoligono().contains(e.getX(), e.getY())) {
                        System.out.println(t.getNome());
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub

            }

            @Override
            public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
         
        //Paint infos
        //g.drawImage(infosImage,0,0,null);
        
        //Paint the sea
        g.drawImage(mapImage,0,0,null);
       
        //Paint Territories/names
        paintTerritories(g);
        
        //Paint territory lines
        g.drawImage(lineImage,0,0,null);
        
        //Paint players
        paintPlayers(g);
    }
    
    public void defineTerritories(){
        for(Territorio t : lstTerritorios){
            //Transforming path and offsetting borders
            t.getPoligono().transform(AffineTransform.getScaleInstance(0.94,0.94));
            t.getPoligono().transform(AffineTransform.getTranslateInstance(3, 3));
        }
    }
    
    private void paintTerritories(Graphics g){
        //Draw the different territories
        Graphics2D g2 = (Graphics2D)g;
        
        //Turn Antialiasing on
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(Territorio t : lstTerritorios){
           
            //Filling
            switch(t.getContinente()){
                case("AN"): g2.setColor(new Color(238,64,54)); break;
                case("AS"): g2.setColor(new Color(0,104,56)); break;
                case("AF"): g2.setColor(new Color(101,45,144)); break;
                case("EU"): g2.setColor(new Color(43,56,143)); break;
                case("ASI"): g2.setColor(new Color(246,146,30)); break;
                case("OC"): g2.setColor(new Color(38,169,224)); break;
            }
            g2.fill((Shape)t.getPoligono());
           
            //Drawing borders
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(Color.BLACK);
            g2.draw(t.getPoligono());
        }
    }
    
    private void paintPlayers(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        
        int xCoord = 20;
        int diameter = 50;
        float alpha;
        Player currentPlayer = Turn.getInstance().getCurrentPlayer();
        
        for(Color c : playerColors){
           
           g2.setPaint(c);
           g2.fillOval(xCoord,20,diameter,diameter);
           
           g2.setColor(Color.BLACK);
           g2.drawOval(xCoord,20,diameter,diameter);
           
           if(currentPlayer.getColor().equals(c)){
               g2.setPaint(Color.BLACK);
               g2.fillOval(xCoord+diameter/4,20+diameter/4,diameter/2,diameter/2);
           }
           
           xCoord += 70;
        }
    }
    // Bloco de inicialização dos territ�rios
    // Estou assumindo que a classe territorio tem um nome e um poligono definindo sua área de clique.
    {
        // Adicionando os territorios na lista de territorios.
        lstTerritorios.add(new Territorio("AN","Alasca", new Ponto[] {
                        new Ponto(86.1, 124.1),
                        new Ponto(134.6, 124.1),
                        new Ponto(111.4, 167.3),
                        new Ponto(58.7, 167.3),
                        new Ponto(72.3, 141.8),
                        new Ponto(77.3, 141.8) 
        }, deslocaX, deslocaY));

        lstTerritorios.add(new Territorio("AN","Calgary", new Ponto[] {
                        new Ponto(148.7, 124.1),
                        new Ponto(134.6, 124.1),
                        new Ponto(126.4, 139.5),
                        new Ponto(146.9, 177.0),
                        new Ponto(223.0, 177.0),
                        new Ponto(231.3, 163.2),
                        new Ponto(245.5, 163.2),
                        new Ponto(258.5, 139.5),
                        new Ponto(284.1, 139.5),
                        new Ponto(271.6, 116.9),
                        new Ponto(241.7, 116.9),
                        new Ponto(230.4, 137.8),
                        new Ponto(212.9, 137.8),
                        new Ponto(208.7, 130.1),
                        new Ponto(152.4, 130.1)
        }, deslocaX, deslocaY));

        lstTerritorios.add(new Territorio("AN","Groelandia", new Ponto[] {
                        new Ponto(285.4, 94.6),
                        new Ponto(271.6, 116.9),
                        new Ponto(284.1, 139.5),
                        new Ponto(328.7, 139.5),
                        new Ponto(335.1, 149.5),
                        new Ponto(350.8, 149.5),
                        new Ponto(365.9, 122.1),
                        new Ponto(371.4, 122.1),
                        new Ponto(380.4, 108.5),
                        new Ponto(371.0, 94.6)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","Vancouver", new Ponto[] {
                        new Ponto(111.4, 167.3),
                        new Ponto(119.3, 182.3),
                        new Ponto(102.1, 211.9),
                        new Ponto(107.5, 220.0),
                        new Ponto(214.0, 220.0),
                        new Ponto(231.7, 191.2),
                        new Ponto(223.0, 177.0),
                        new Ponto(146.90, 177.0),
                        new Ponto(126.4, 139.5)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","Quebec", new Ponto[] {
                        new Ponto(262.2, 190.8),
                        new Ponto(231.7, 190.8),
                        new Ponto(214.0, 220.0),
                        new Ponto(296.9, 220.0),
                        new Ponto(304.3, 207.1),
                        new Ponto(316.1, 207.1),
                        new Ponto(312.5, 215.3),
                        new Ponto(325.1, 215.3),
                        new Ponto(329.9, 204.5),
                        new Ponto(322.8, 191.4),
                        new Ponto(331.6, 177.8),
                        new Ponto(337.1, 184.7),
                        new Ponto(344.7, 172.2),
                        new Ponto(340.5, 163.4),
                        new Ponto(321.2, 163.4),
                        new Ponto(318.8, 160.4),
                        new Ponto(288.7, 160.4),
                        new Ponto(281.9, 171.9),
                        new Ponto(272.9, 171.9)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","California", new Ponto[] {
                        new Ponto(107.5, 220.0),
                        new Ponto(84.7, 258.0),
                        new Ponto(89.9, 268.9),
                        new Ponto(78.2, 289.0),
                        new Ponto(90.4, 310.2),
                        new Ponto(126.4, 310.2),
                        new Ponto(178.5, 220.0)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","Texas", new Ponto[] {
                        new Ponto(202.2, 248.2),
                        new Ponto(231.7, 248.2),
                        new Ponto(246.1, 220.0),
                        new Ponto(178.5, 220.0),
                        new Ponto(126.4, 310.2),
                        new Ponto(145.9, 343.8)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","NovaYork", new Ponto[] {
                        new Ponto(181.8, 310.2),
                        new Ponto(165.7, 310.2),
                        new Ponto(202.2, 248.2),
                        new Ponto(231.7, 248.2),
                        new Ponto(246.1, 220.0),
                        new Ponto(296.9, 220.0),
                        new Ponto(290.7, 231.7),
                        new Ponto(279.4, 231.7),
                        new Ponto(261.5, 265.1),
                        new Ponto(243.7, 265.1),
                        new Ponto(237.8, 279.1),
                        new Ponto(231.3, 279.1),
                        new Ponto(215.3, 306.5),
                        new Ponto(207.3, 306.5),
                        new Ponto(200.6, 318.9),
                        new Ponto(211.5, 337.8),
                        new Ponto(204.3, 349.4)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AN","Mexico", new Ponto[] {
                        new Ponto(90.4, 310.2),
                        new Ponto(83.1, 323.0),
                        new Ponto(103.6, 359.2),
                        new Ponto(110.6, 346.5),
                        new Ponto(102.5, 330.6),
                        new Ponto(105.7, 324.8),
                        new Ponto(136.6, 377.6),
                        new Ponto(145.7, 377.6),
                        new Ponto(162.1, 408.2),
                        new Ponto(172.2, 408.2),
                        new Ponto(179.8, 421.9),
                        new Ponto(188.8, 406.2),
                        new Ponto(183.8, 398.0),
                        new Ponto(178.5, 398.0),
                        new Ponto(173.0, 389.4),
                        new Ponto(173.0, 386.3),
                        new Ponto(175.3, 384.3),
                        new Ponto(167.1, 370.0),
                        new Ponto(171.8, 360.7),
                        new Ponto(164.3, 348.1),
                        new Ponto(161.4, 355.3),
                        new Ponto(152.3, 355.3),
                        new Ponto(126.4, 310.2)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AS","Venezuela", new Ponto[] {
                        new Ponto(193.5, 398.2),
                        new Ponto(254.3, 398.2),
                        new Ponto(196.8, 498.1),
                        new Ponto(188.8, 482.2),
                        new Ponto(176.2, 482.2),
                        new Ponto(161.2, 455.8),
                        new Ponto(179.8, 421.9),
                        new Ponto(188.8, 406.2)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AS","Peru", new Ponto[] {
                        new Ponto(196.8, 498.1),
                        new Ponto(202.3, 507.9),
                        new Ponto(216.1, 507.9),
                        new Ponto(232.2, 538.5),
                        new Ponto(226.8, 545.4),
                        new Ponto(239.7, 564.8),
                        new Ponto(274.9, 505.0),
                        new Ponto(233.8, 433.8)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AS","Brasil", new Ponto[] {
                        new Ponto(259.6, 407.2),
                        new Ponto(285.3, 407.2),
                        new Ponto(293.4, 423.9),
                        new Ponto(305.7, 423.9),
                        new Ponto(316.1, 445.2),
                        new Ponto(335.4, 445.2),
                        new Ponto(343.3, 461.0),
                        new Ponto(343.3, 463.1),
                        new Ponto(325.9, 491.3),
                        new Ponto(333.8, 505.0),
                        new Ponto(274.9, 505.0),
                        new Ponto(233.8, 433.8),
                        new Ponto(254.3, 398.2)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AS","Argentina", new Ponto[] {
                        new Ponto(333.8, 505.0),
                        new Ponto(302.4, 558.8),
                        new Ponto(311.2, 573.0),
                        new Ponto(281.6, 624.5),
                        new Ponto(295.7, 650.2),
                        new Ponto(278.1, 650.2),
                        new Ponto(235.0, 572.4),
                        new Ponto(239.7, 564.8),
                        new Ponto(274.9, 505.0)
        }, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Africa do Sul", new Ponto[] { new Ponto(528.4, 545.4),
                        new Ponto(558.1, 600.2),
                        new Ponto(597.1, 600.2),
                        new Ponto(606.1, 584.7),
                        new Ponto(613.1, 584.7),
                        new Ponto(626.6, 559.1),
                        new Ponto(617.6, 545.4)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Angola", new Ponto[] {
                        new Ponto(514.7, 493.2),
                        new Ponto(519.9, 482.5),
                        new Ponto(588.6, 482.5),
                        new Ponto(599.4, 502.3),
                        new Ponto(573.4, 545.4),
                        new Ponto(528.4, 545.4),
                        new Ponto(535.6, 531.4)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Argelia", new Ponto[] {
                        new Ponto(436.7, 337.5),
                        new Ponto(473.2, 337.5),
                        new Ponto(479.6, 350.3),
                        new Ponto(492.5, 350.3),
                        new Ponto(499.1, 364.1),
                        new Ponto(539.8, 364.1),
                        new Ponto(513.0, 410.1),
                        new Ponto(425.3, 410.1),
                        new Ponto(409.3, 384.5)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Egito", new Ponto[] {
                        new Ponto(588.6, 360.3),
                        new Ponto(542.0, 360.3),
                        new Ponto(539.8, 364.1),
                        new Ponto(513.0, 410.1),
                        new Ponto(547.6, 410.1),
                        new Ponto(562.4, 436.6),
                        new Ponto(623.6, 436.6),
                        new Ponto(594.0, 385.3),
                        new Ponto(597.8, 378.5)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Nigeria", new Ponto[] {
                        new Ponto(425.3, 410.1),
                        new Ponto(448.1, 452.2),
                        new Ponto(503.3, 452.2),
                        new Ponto(519.9, 482.5),
                        new Ponto(588.6, 482.5),
                        new Ponto(562.4, 436.6),
                        new Ponto(547.6, 410.1)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("AF","Somalia", new Ponto[] {
                        new Ponto(643.1, 502.0),
                        new Ponto(653.0, 502.0),
                        new Ponto(672.7, 464.6),
                        new Ponto(639.3, 464.6),
                        new Ponto(623.6, 436.6),
                        new Ponto(562.4, 436.6),
                        new Ponto(599.4, 502.3),
                        new Ponto(573.4, 545.4),
                        new Ponto(617.6, 545.4)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Espanha", new Ponto[] {
                        new Ponto(442.3, 266.0),
                        new Ponto(461.0, 266.0),
                        new Ponto(476.9, 292.5),
                        new Ponto(475.0, 296.6),
                        new Ponto(480.0, 296.6),
                        new Ponto(470.7, 312.3),
                        new Ponto(452.3, 312.3),
                        new Ponto(456.4, 304.0),
                        new Ponto(443.2, 304.0),
                        new Ponto(440.2, 309.2),
                        new Ponto(415.9, 309.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Franca", new Ponto[] {
                        new Ponto(526.8, 186.6),
                        new Ponto(538.7, 210.4),
                        new Ponto(508.1, 263.1),
                        new Ponto(502.1, 263.1),
                        new Ponto(494.4, 279.1),
                        new Ponto(483.7, 279.1),
                        new Ponto(476.9, 292.5),
                        new Ponto(461.0, 266.0),
                        new Ponto(466.7, 255.2),
                        new Ponto(459.9, 244.7),
                        new Ponto(452.3, 244.7),
                        new Ponto(448.2, 235.7),
                        new Ponto(488.6, 235.7),
                        new Ponto(499.3, 219.1),
                        new Ponto(516.2, 219.1),
                        new Ponto(521.8, 207.7),
                        new Ponto(517.8, 200.4)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Italia", new Ponto[] {
                        new Ponto(552.4, 210.4),
                        new Ponto(538.7, 210.4),
                        new Ponto(508.1, 263.1),
                        new Ponto(516.6, 263.1),
                        new Ponto(526.7, 281.8),
                        new Ponto(533.7, 281.8),
                        new Ponto(542.5, 298.2),
                        new Ponto(542.5, 300.2),
                        new Ponto(537.1, 308.2),
                        new Ponto(549.2, 308.2),
                        new Ponto(554.8, 297.3),
                        new Ponto(556.1, 297.3),
                        new Ponto(557.4, 299.5),
                        new Ponto(563.9, 299.5),
                        new Ponto(563.9, 297.7),
                        new Ponto(552.8, 277.8),
                        new Ponto(547.0, 277.8),
                        new Ponto(539.1, 261.1),
                        new Ponto(543.5, 253.4),
                        new Ponto(552.8, 253.4),
                        new Ponto(555.8, 259.4),
                        new Ponto(567.5, 237.9)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Polonia", new Ponto[] {
                        new Ponto(575.6, 176.1),
                        new Ponto(583.6, 176.0),
                        new Ponto(600.8, 205.9),
                        new Ponto(581.8, 237.9),
                        new Ponto(567.5, 237.9),
                        new Ponto(552.4, 210.4),
                        new Ponto(564.1, 189.0),
                        new Ponto(569.8, 189.0)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Reino Unido", new Ponto[] {
                        new Ponto(462.0, 146.2),
                        new Ponto(481.6, 146.2),
                        new Ponto(473.7, 156.6),
                        new Ponto(480.8, 156.6),
                        new Ponto(470.3, 174.2),
                        new Ponto(482.7, 198.4),
                        new Ponto(490.8, 198.4),
                        new Ponto(484.0, 210.4),
                        new Ponto(445.5, 210.4),
                        new Ponto(451.3, 196.1),
                        new Ponto(458.1, 196.1),
                        new Ponto(463.2, 188.5),
                        new Ponto(454.0, 174.2),
                        new Ponto(459.4, 165.3),
                        new Ponto(459.4, 163.2),
                        new Ponto(450.8, 163.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Romania", new Ponto[] {
                        new Ponto(567.5, 237.9),
                        new Ponto(555.8, 259.4),
                        new Ponto(555.8, 262.4),
                        new Ponto(557.6, 264.2),
                        new Ponto(566.1, 264.2),
                        new Ponto(579.8, 288.6),
                        new Ponto(574.4, 297.5),
                        new Ponto(581.4, 308.2),
                        new Ponto(593.2, 308.2),
                        new Ponto(601.4, 297.4),
                        new Ponto(595.1, 289.5),
                        new Ponto(598.3, 283.7),
                        new Ponto(607.9, 283.7),
                        new Ponto(581.8, 237.9)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Suecia", new Ponto[] {
                        new Ponto(551.0, 96.6),
                        new Ponto(582.5, 96.6),
                        new Ponto(610.2, 144.5),
                        new Ponto(602.0, 158.0),
                        new Ponto(571.5, 158.0),
                        new Ponto(578.7, 145.7),
                        new Ponto(569.6, 145.7),
                        new Ponto(579.6, 127.8),
                        new Ponto(562.9, 127.8),
                        new Ponto(552.1, 145.8),
                        new Ponto(558.1, 156.3),
                        new Ponto(552.0, 167.3),
                        new Ponto(555.4, 172.1),
                        new Ponto(548.3, 183.7),
                        new Ponto(535.8, 183.7),
                        new Ponto(530.4, 171.1),
                        new Ponto(521.7, 171.1),
                        new Ponto(514.0, 183.7),
                        new Ponto(501.4, 183.7),
                        new Ponto(492.1, 166.6),
                        new Ponto(507.0, 140.6),
                        new Ponto(522.0, 140.6),
                        new Ponto(537.1, 111.5),
                        new Ponto(543.0, 111.5)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("EU","Ucrania", new Ponto[] {
                        new Ponto(611.6, 254.4),
                        new Ponto(620.1, 239.5),
                        new Ponto(600.8, 205.9),
                        new Ponto(581.8, 237.9),
                        new Ponto(607.9, 283.7),
                        new Ponto(619.0, 264.9)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Arabia Saudita", new Ponto[] {
                        new Ponto(646.2, 423.6),
                        new Ponto(639.1, 434.7),
                        new Ponto(649.1, 450.3),
                        new Ponto(699.0, 450.3),
                        new Ponto(726.6, 403.0),
                        new Ponto(716.8, 387.3),
                        new Ponto(680.5, 387.3),
                        new Ponto(654.0, 342.4),
                        new Ponto(627.4, 388.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Bangladesh", new Ponto[] {
                        new Ponto(885.3, 350.0),
                        new Ponto(847.8, 350.0),
                        new Ponto(828.7, 383.5),
                        new Ponto(842.1, 410.9),
                        new Ponto(848.1, 410.9),
                        new Ponto(859.7, 432.6),
                        new Ponto(854.7, 441.3),
                        new Ponto(870.5, 470.8),
                        new Ponto(879.3, 458.8),
                        new Ponto(879.3, 456.0),
                        new Ponto(872.2, 444.6),
                        new Ponto(879.9, 432.5),
                        new Ponto(859.2, 396.3)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Cazaquistao", new Ponto[] {
                        new Ponto(907.9, 201.9),
                        new Ponto(920.4, 222.6),
                        new Ponto(906.1, 246.4),
                        new Ponto(784.1, 246.4),
                        new Ponto(772.1, 224.1),
                        new Ponto(724.7, 224.1),
                        new Ponto(739.0, 201.9)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Mongolia", new Ponto[] {
                        new Ponto(906.1, 246.4),
                        new Ponto(804.9, 246.4),
                        new Ponto(822.0, 278.5),
                        new Ponto(873.9, 278.5),
                        new Ponto(894.6, 278.5),
                        new Ponto(906.1, 300.4),
                        new Ponto(916.3, 287.4),
                        new Ponto(902.8, 264.5),
                        new Ponto(910.3, 254.7)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","China", new Ponto[] {
                        new Ponto(873.9, 278.5),
                        new Ponto(822.0, 278.5),
                        new Ponto(804.9, 246.5),
                        new Ponto(784.1, 246.5),
                        new Ponto(773.5, 264.0),
                        new Ponto(764.7, 278.9),
                        new Ponto(754.8, 297.2),
                        new Ponto(785.5, 348.8),
                        new Ponto(785.5, 350.5),
                        new Ponto(811.9, 350.5),
                        new Ponto(838.9, 302.5),
                        new Ponto(887.9, 302.5)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Coreia do Norte", new Ponto[] { 
                        new Ponto(839.0, 302.2),
                        new Ponto(825.3, 326.1),
                        new Ponto(914.9, 326.1),
                        new Ponto(908.2, 314.2),
                        new Ponto(894.1, 314.2),
                        new Ponto(888.0, 302.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Coreia do Sul", new Ponto[] {
                        new Ponto(914.9, 326.1),
                        new Ponto(922.1, 337.2),
                        new Ponto(915.5, 350.0),
                        new Ponto(811.9, 350.0),
                        new Ponto(825.3, 326.1)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Estonia", new Ponto[] {
                        new Ponto(735.3, 123.8),
                        new Ponto(659.5, 123.8),
                        new Ponto(650.1, 144.1),
                        new Ponto(630.0, 144.1),
                        new Ponto(614.8, 117.0),
                        new Ponto(628.2, 117.0),
                        new Ponto(632.0, 124.2),
                        new Ponto(644.4, 124.2),
                        new Ponto(632.6, 101.5),
                        new Ponto(607.8, 101.5),
                        new Ponto(604.9, 96.6),
                        new Ponto(582.5, 96.6),
                        new Ponto(626.8, 173.1),
                        new Ponto(706.8, 173.1)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","India", new Ponto[] {
                        new Ponto(798.0, 450.3),
                        new Ponto(763.8, 387.8),
                        new Ponto(785.5, 351.2),
                        new Ponto(785.5, 350.0),
                        new Ponto(847.8, 350.0),
                        new Ponto(808.0, 420.0),
                        new Ponto(812.3, 427.5)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("ASI","Ira", new Ponto[] {
                        new Ponto(716.8, 310.2),
                        new Ponto(701.1, 310.2),
                        new Ponto(691.0, 329.0),
                        new Ponto(703.0, 351.2),
                        new Ponto(716.5, 375.9),
                        new Ponto(733.6, 375.9),
                        new Ponto(739.9, 387.8),
                        new Ponto(751.3, 370.1)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Iraque", new Ponto[] {
                        new Ponto(703.0, 351.2),
                        new Ponto(694.0, 362.7),
                        new Ponto(694.0, 364.8),
                        new Ponto(706.5, 387.3),
                        new Ponto(680.5, 387.3),
                        new Ponto(654.0, 342.4),
                        new Ponto(671.9, 310.2),
                        new Ponto(701.1, 310.2),
                        new Ponto(691.0, 329.0)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Japao", new Ponto[] {
                        new Ponto(937.3, 222.4),
                        new Ponto(956.3, 254.3),
                        new Ponto(953.7, 257.5),
                        new Ponto(965.5, 276.8),
                        new Ponto(955.4, 293.3),
                        new Ponto(947.6, 293.3),
                        new Ponto(940.0, 307.4),
                        new Ponto(921.9, 307.4),
                        new Ponto(930.2, 293.0),
                        new Ponto(927.9, 288.6),
                        new Ponto(933.8, 279.7),
                        new Ponto(939.4, 279.7),
                        new Ponto(943.7, 269.9),
                        new Ponto(927.1, 239.4)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Jordania", new Ponto[] {
                        new Ponto(621.9, 378.5),
                        new Ponto(612.8, 378.5),
                        new Ponto(602.1, 357.1),
                        new Ponto(615.7, 337.6),
                        new Ponto(633.6, 337.6),
                        new Ponto(649.1, 310.2),
                        new Ponto(671.9, 310.2),
                        new Ponto(654.0, 342.4),
                        new Ponto(627.4, 388.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Letonia", new Ponto[] {
                        new Ponto(610.2, 144.5),
                        new Ponto(591.0, 176.0),
                        new Ponto(583.6, 176.0),
                        new Ponto(611.2, 224.1),
                        new Ponto(724.7, 224.1),
                        new Ponto(739.0, 201.9),
                        new Ponto(724.7, 173.1),
                        new Ponto(626.8, 173.1)}, deslocaX,deslocaY));



        lstTerritorios.add(new Territorio("ASI","Paquistao", new Ponto[] {
                        new Ponto(763.8, 387.8),
                        new Ponto(739.9, 387.8),
                        new Ponto(751.3, 370.1),
                        new Ponto(708.9, 296.4),
                        new Ponto(718.3, 278.9),
                        new Ponto(764.7, 278.9),
                        new Ponto(754.8, 297.2),
                        new Ponto(785.5, 348.8),
                        new Ponto(785.5, 351.2)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Russia", new Ponto[] {
                        new Ponto(744.4, 125.0),
                        new Ponto(742.5, 129.9),
                        new Ponto(748.2, 129.9),
                        new Ponto(744.1, 136.7),
                        new Ponto(728.4, 136.7),
                        new Ponto(706.7, 173.1),
                        new Ponto(724.7, 173.1),
                        new Ponto(739.0, 201.9),
                        new Ponto(825.0, 201.9),
                        new Ponto(869.9, 125.0)}, deslocaX,deslocaY));

        lstTerritorios.add(new Territorio("ASI","Siberia", new Ponto[] {
                        new Ponto(949.4, 204.5),
                        new Ponto(956.8, 191.1),
                        new Ponto(940.9, 162.5),
                        new Ponto(947.1, 155.2),
                        new Ponto(940.9, 144.4),
                        new Ponto(954.0, 144.4),
                        new Ponto(937.8, 117.0),
                        new Ponto(875.3, 117.0),
                        new Ponto(870.6, 124.9),
                        new Ponto(825.0, 201.9),
                        new Ponto(888.4, 201.9),
                        new Ponto(877.3, 187.8),
                        new Ponto(885.4, 176.1),
                        new Ponto(907.5, 176.1),
                        new Ponto(916.3, 159.9),
                        new Ponto(930.5, 183.1),
                        new Ponto(938.0, 183.1)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("ASI","Siria", new Ponto[] {
                        new Ponto(660.8, 272.8),
                        new Ponto(664.6, 278.9),
                        new Ponto(718.3, 278.9),
                        new Ponto(708.9, 296.4),
                        new Ponto(716.8, 310.2),
                        new Ponto(637.0, 310.2),
                        new Ponto(637.0, 306.2),
                        new Ponto(628.7, 306.6),
                        new Ponto(619.6, 291.5),
                        new Ponto(628.7, 275.8),
                        new Ponto(646.1, 275.8),
                        new Ponto(647.3, 272.8)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("ASI","Tailandia", new Ponto[] {
                        new Ponto(915.5, 350.0),
                        new Ponto(885.3, 350.0),
                        new Ponto(859.2, 396.3),
                        new Ponto(879.9, 432.5),
                        new Ponto(887.3, 446.2),
                        new Ponto(895.1, 446.2),
                        new Ponto(901.8, 432.5),
                        new Ponto(897.5, 426.0),
                        new Ponto(901.8, 420.7),
                        new Ponto(887.0, 398.1),
                        new Ponto(892.9, 389.8),
                        new Ponto(895.9, 395.1),
                        new Ponto(908.7, 395.1),
                        new Ponto(911.4, 387.6),
                        new Ponto(919.1, 387.6),
                        new Ponto(927.4, 370.3)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("ASI","Turquia", new Ponto[] {
                        new Ponto(683.6, 278.9),
                        new Ponto(691.0, 268.9),
                        new Ponto(678.5, 247.4),
                        new Ponto(668.1, 247.4),
                        new Ponto(664.5, 240.9),
                        new Ponto(651.9, 240.9),
                        new Ponto(646.3, 251.2),
                        new Ponto(639.4, 239.5),
                        new Ponto(620.1, 239.5),
                        new Ponto(611.2, 224.1),
                        new Ponto(772.1, 224.1),
                        new Ponto(784.1, 246.4),
                        new Ponto(764.7, 278.9)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("OC","Autr�lia", new Ponto[] {
                        new Ponto(875.1, 539.4),
                        new Ponto(885.9, 539.4),
                        new Ponto(919.7, 598.0),
                        new Ponto(911.5, 611.8),
                        new Ponto(917.6, 623.4),
                        new Ponto(901.8, 650.0),
                        new Ponto(891.1, 650.0),
                        new Ponto(877.6, 673.5),
                        new Ponto(846.1, 673.5),
                        new Ponto(837.9, 655.8),
                        new Ponto(823.4, 655.8),
                        new Ponto(816.7, 641.9)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("OC","Indonesia", new Ponto[] {
                        new Ponto(850.9, 480.7),
                        new Ponto(861.3, 500.4),
                        new Ponto(880.7, 500.4),
                        new Ponto(887.7, 486.3),
                        new Ponto(902.5, 486.3),
                        new Ponto(907.6, 498.3),
                        new Ponto(925.2, 498.3),
                        new Ponto(935.6, 517.9),
                        new Ponto(943.8, 517.9),
                        new Ponto(954.3, 536.6),
                        new Ponto(928.3, 536.6),
                        new Ponto(920.3, 522.0),
                        new Ponto(907.2, 522.0),
                        new Ponto(904.7, 528.9),
                        new Ponto(890.3, 528.9),
                        new Ponto(885.3, 517.5),
                        new Ponto(849.0, 517.5),
                        new Ponto(835.2, 490.0),
                        new Ponto(841.0, 480.7)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("OC","Nova Zelandia", new Ponto[] {
                        new Ponto(928.8, 601.5),
                        new Ponto(936.9, 601.5),
                        new Ponto(943.0, 613.7),
                        new Ponto(939.8, 616.5),
                        new Ponto(944.2, 616.5),
                        new Ponto(950.3, 628.6),
                        new Ponto(931.9, 661.5),
                        new Ponto(926.6, 661.5),
                        new Ponto(917.6, 678.5),
                        new Ponto(900.1, 678.5),
                        new Ponto(917.6, 644.5),
                        new Ponto(922.9, 644.5),
                        new Ponto(932.3, 627.8),
                        new Ponto(928.0, 619.1),
                        new Ponto(934.1, 610.0)}, deslocaX,deslocaY));

        lstTerritorios.add( new Territorio("OC","Perth", new Ponto[] {
                        new Ponto(856.7, 535.5),
                        new Ponto(839.2, 535.5),
                        new Ponto(822.3, 565.8),
                        new Ponto(799.1, 565.8),
                        new Ponto(789.6, 584.3),
                        new Ponto(781.3, 584.3),
                        new Ponto(775.3, 595.7),
                        new Ponto(775.3, 598.3),
                        new Ponto(782.3, 608.6),
                        new Ponto(782.3, 610.7),
                        new Ponto(770.9, 630.1),
                        new Ponto(763.5, 630.1),
                        new Ponto(756.7, 639.9),
                        new Ponto(766.4, 655.8),
                        new Ponto(780.3, 655.8),
                        new Ponto(787.3, 641.9),
                        new Ponto(816.7, 641.9),
                        new Ponto(861.6, 562.1),
                        new Ponto(855.6, 550.2),
                        new Ponto(862.4, 542.3)}, deslocaX,deslocaY));

    }
}