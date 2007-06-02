/**
 * @version 23/01/2003 <BR>
 * @author Setpoint Inform�tica Ltda./Fernando Oliveira da Silva <BR>
 *
 * Projeto: Freedom <BR>
 * Pacote: org.freedom.componentes <BR>
 * Classe: @(#)PainelImagem.java <BR>
 * 
 * Este programa � licenciado de acordo com a LPG-PC (Licen�a P�blica Geral para Programas de Computador), <BR>
 * vers�o 2.1.0 ou qualquer vers�o posterior. <BR>
 * A LPG-PC deve acompanhar todas PUBLICA��ES, DISTRIBUI��ES e REPRODU��ES deste Programa. <BR>
 * Caso uma c�pia da LPG-PC n�o esteja dispon�vel junto com este Programa, voc� pode contatar <BR>
 * o LICENCIADOR ou ent�o pegar uma c�pia em: <BR>
 * Licen�a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa � preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR> <BR>
 *
 * Coment�rios da classe.....
 */


package org.freedom.componentes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import org.freedom.componentes.JPanelPad;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import org.freedom.funcoes.Funcoes;
import org.freedom.telas.FZoom;

public class PainelImagem extends JPanelPad implements ActionListener, MouseListener {

  private static final long serialVersionUID = 1L;
  public static final int TP_NONE = -1;
  public static final int TP_BYTES = 11;
  private Image imImagem = null;
  private FileInputStream fisImagem = null;
  private byte[] bImagem = null;
  private ListaCampos lcPainel = null;
  private File fImagem = null;
  private DadosImagem diImagem;
  private Component cOrigem = null;
  public int iTipo = -1;
  public int iTam = 0;
  private boolean bAlt = false;
  private boolean bNulo = true;
  private JPopupMenu pm = new JPopupMenu();
  private JMenuItem selecMI = new JMenuItem();
  private JMenuItem excluirMI = new JMenuItem();
  private JMenuItem zoomMI = new JMenuItem();
  private JFrame jfPai = null;
  private int iDescBordA = 0;
  private int iDescBordL = 0;
  private int iPercAtual = 100;
  private int iMaxSize = 65000;
  private boolean bStretch = false;
  private boolean bEncaixa = true;
  private boolean bZoom = true;
  private boolean bMudaTamanho = false;
  private int L = 0;
  private int A = 0;
  public boolean ehNulo() {
	  return bNulo;
  }
  public PainelImagem(int iTam) {
  	this(null,iTam);
  }
  public PainelImagem(Component cOwner,int  iTam) {
  	this.iTam = iTam;
    if (cOwner != null) {
      cOrigem = cOwner;
      bMudaTamanho = true;
    }
    else {
      cOrigem = this;
    }
    setBackground(Color.white);

    selecMI.setText("Selecionar Arquivo");
    selecMI.setMnemonic('S');
    selecMI.addActionListener(this);
    
    excluirMI.setText("Excluir");
    excluirMI.setMnemonic('E');
    excluirMI.addActionListener(this);
    
    zoomMI.setText("Zoom");
    zoomMI.setMnemonic('Z');
    zoomMI.addActionListener(this);
    
    pm.add(selecMI);
    pm.add(excluirMI);
    pm.add(zoomMI);

    setBorder(javax.swing.BorderFactory.createEtchedBorder());
    addMouseListener(this);
  }
  public void setMaxSize(int iVal) {
    iMaxSize = iVal;
  }
  public int getMaxSize() {
    return iMaxSize;
  }
  public int getZoom() {
    return iPercAtual;
  }
  public void setStretch(boolean b) {
    bStretch = b;
    bZoom = false;
  }
  public boolean getStretch() {
    return bStretch;
  }
  public void setEncaixa(boolean b) {
    bEncaixa = b;
    bZoom = false;
  }
  public boolean getEncaixa() {
    return bEncaixa;
  }
  public int getAltura() {
    int iRetorno = 0;
    if (imImagem != null)
      iRetorno = imImagem.getHeight(this);
    return iRetorno;
  }
  public int getLargura() {
    int iRetorno = 0;
    if (imImagem != null)
      iRetorno = imImagem.getWidth(this);
    return iRetorno;
  }
  public void setBorder(Border bor) {
    iDescBordA = 2;
    iDescBordL = 2;
    super.setBorder(bor);
  }
  public void setListaCampos(ListaCampos lc) {
    lcPainel = lc;
  }
  
  public void setVlrBytes(Image imF) {
    imImagem = imF;
    if (lcPainel != null) {
      lcPainel.edit();      
    }
    bZoom = false;
  }
  
  public void setVlrBytes(byte[] bVals) {
    bImagem = bVals;
    imImagem = (new ImageIcon( bImagem ) ).getImage();    
    if ( diImagem == null ) {
      diImagem = new DadosImagem(fisImagem,0,bVals);
    }
    else {
      diImagem.setBytes(bVals);
    }
    bAlt = false;
    bZoom = false;
    repaint();
  }
  public void setVlrBytes(InputStream is) {
  	if (is == null)
  		return;
  	byte[] bVals = new byte[iTam];
  	BufferedInputStream bi = new BufferedInputStream(is);
  	try {
  	  bi.read(bVals,0,bVals.length);
  	}
  	catch(IOException err) {
  		Funcoes.mensagemErro(null,"Erro ao carregar bytes!\n"+err.getMessage());
  		err.printStackTrace();
  	}
  	bImagem = bVals;
  	imImagem = (new ImageIcon( bImagem ) ).getImage();    
  	if ( diImagem == null ) {
  		diImagem = new DadosImagem(fisImagem,0,bVals);
  	}
  	else {
  		diImagem.setBytes(bVals);
  	}
  	bAlt = false;
  	bZoom = false;
  	repaint();
  }
  public int[] getValsEncaixa(Image im) {
    int[] iRetorno = new int[2];
    if (im != null) {
      
    }
    return iRetorno;
  }
  public boolean foiAlterado() {
    return bAlt;
  }
  public DadosImagem getVlrBytes() {
    bAlt = false;
    return diImagem;
  }
  
/*  public void update(Graphics g) {
    g.drawImage(imImagem,0,0,this);
//    super.update(g);
  }*/
  
  public void paint(Graphics Screen) {
    super.paint(Screen);
    if (imImagem !=null) {
      if (bZoom) {
        paintZoom(Screen);
      }
      else {
        if (bStretch) {
          bZoom = false;
          Screen.drawImage(imImagem,iDescBordL,iDescBordA,((int)cOrigem.getSize().getWidth())-(iDescBordL+1),((int)cOrigem.getSize().getHeight())-(iDescBordA+1),this);
        }
        else {
          if (bEncaixa) {
            setZoom(getPercEncaixa());
//            System.out.println(getPercEncaixa());
            if ((L > A) | (L == A))
              L = L > ((int)cOrigem.getSize().getWidth()) ? L-(L - ((int)cOrigem.getSize().getWidth())) : L+(((int)cOrigem.getSize().getWidth()) - L);
            else if (A > L)
              A = A > ((int)cOrigem.getSize().getHeight()) ? A-(A - ((int)cOrigem.getSize().getHeight())) : A+(((int)cOrigem.getSize().getHeight()) - A);
            showZoom(Screen);
          }
          else {
            bZoom = false;
            Screen.drawImage(imImagem,0,0,this);
            if (bMudaTamanho) {
              setPreferredSize(new Dimension(imImagem.getWidth(this),imImagem.getHeight(this)));
            }
          }
        }
      }
    }
  }
  public void setZoom(int iZ) {
    bZoom = true;
    iPercAtual = iZ;
    L = (imImagem.getWidth(this)/100)*iZ;
    A = (imImagem.getHeight(this)/100)*iZ;
  }
  public void showZoom(Graphics Screen) {
    Screen.drawImage(imImagem,iDescBordA,iDescBordA,L-(iDescBordL+1),A-(iDescBordA+1),this);
    if (bMudaTamanho) {
      setPreferredSize(new Dimension(L,A));
    }
  }
  public void paintZoom(Graphics Screen) {
    Screen.drawImage(imImagem,iDescBordA,iDescBordA,L-(iDescBordL+1),A-(iDescBordA+1),this);
    if (bMudaTamanho)
      setPreferredSize(new Dimension(L,A));
  }
  public int getPercEncaixa() {
    int iLargImagem = imImagem.getWidth(this);
    int iAltImagem = imImagem.getHeight(this);
    int iLargPainel = (int)cOrigem.getSize().getWidth();
    int iAltPainel = (int)cOrigem.getSize().getHeight();
    int iRetorno = 0;
    if ((iLargImagem > iAltImagem) | (iLargImagem == iAltImagem)){
      if (iLargImagem > iLargPainel) {
        iRetorno = 100 - (int)(((double)iLargImagem - iLargPainel)*((double)100/iLargImagem));
      }
      else if (iLargImagem < iLargPainel) {
        iRetorno = 100 + (int)(((double)iLargPainel - iLargImagem)*((double)100/iLargImagem));
//        System.out.println("Retorno: "+iRetorno);
      }
      else if (iLargImagem == iLargPainel) {
        iRetorno = 100;
      }
    }
    else if (iAltImagem > iLargImagem) {
      if (iLargImagem > iLargPainel) {
        iRetorno = 100 - (int)(((double)iAltImagem - iAltPainel)*((double)100/iAltImagem));
      }
      else if (iLargImagem < iLargPainel) {
        iRetorno = 100 + (int)(((double)iAltPainel - iAltImagem)*((double)100/iAltImagem));
      }
      else if (iAltImagem == iAltPainel) {
        iRetorno = 100;
      }
    }
    return iRetorno;
  }
  private void excluir() {
    setVlrBytes((new ImageIcon(new byte[0])).getImage());
    repaint();
  }
  private void selec() {
//    byte[] bRetorno = null;
    boolean bEncont = false;
    boolean bCancel = false;
    Component comp = null;
    String sFileName = "";
    FileDialog fdImagem = null;
    comp = getParent();
    while (true) {
      if (comp.getParent() == null) {
        break;
      }
      comp = comp.getParent();
      if (comp instanceof JFrame) {
        bEncont = true;
        break;
      }
    }
    if (bEncont) {
      jfPai = (JFrame)comp;
//      Funcoes.mensagemErro(null,"Encontrou!!:"+jfPai.getName());
    }
    if (jfPai != null) {
      try {
        fdImagem = new FileDialog(jfPai,"Abrir Imagem");
        fdImagem.setVisible(true);
        if (fdImagem.getFile() == null) {
          bCancel = true;
        }
        if (!bCancel) {
          sFileName = fdImagem.getDirectory()+fdImagem.getFile();
//          Funcoes.mensagemInforma(null,"Arquivo: "+sFileName);
          fImagem = new File(sFileName);
          if ((int)fImagem.length() > iMaxSize) {
            Funcoes.mensagemErro(null,"O Tamanho do arquivo ultrapassa o limite m�ximo.\n"+
                                               "Tamanho m�ximo permitido: "+iMaxSize+" bytes.\n"+
                                               "Tamanho do arquivo: "+fImagem.length()+" bytes.");
            bCancel = true;
            bNulo = true;
          }
          else {
        	bNulo = false;  
            fisImagem = new FileInputStream(fImagem);
            imImagem = (new ImageIcon(sFileName)).getImage();
            diImagem = new DadosImagem(fisImagem,(int) fImagem.length(),null);
          }
        }
      }
      catch(IOException err) {
      Funcoes.mensagemErro(null,"Erro ao carregar o arquivo de imagem!\n"+err.getMessage());
      }
    }
    if (!bCancel) {
      bAlt = true;
      setVlrBytes(imImagem);
      repaint();
    }
  }
  private void zoom() {
    FZoom zoom = null;
    if (imImagem != null) {
      zoom = new FZoom(imImagem,iTam);    
      zoom.setVisible(true);
    }
  }
  public void actionPerformed(ActionEvent evt) {  
    if (evt.getSource() == excluirMI) excluir();
    else if (evt.getSource() == selecMI) selec();
    else if (evt.getSource() == zoomMI) zoom();
  }
  public void mousePressed(MouseEvent mevt) {
    if (mevt.getModifiers() == InputEvent.BUTTON3_MASK) 
      pm.show( this, mevt.getX(), mevt.getY());  
  }
  public void mouseClicked(MouseEvent mevt) { } 
  public void mouseEntered(MouseEvent mevt) { } 
  public void mouseExited(MouseEvent mevt) { } 
  public void mouseReleased(MouseEvent mevt) { } 
}
