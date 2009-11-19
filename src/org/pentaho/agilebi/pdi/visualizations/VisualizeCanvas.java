package org.pentaho.agilebi.pdi.visualizations;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.pentaho.agilebi.pdi.visualizations.web.WebVisualization;
import org.pentaho.agilebi.pdi.visualizations.web.WebVisualizationBrowser;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.EngineMetaInterface;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.gui.SpoonFactory;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.tableoutput.TableOutputMeta;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.spoon.TabItemInterface;
import org.pentaho.di.ui.spoon.TabMapEntry;
import org.pentaho.di.ui.util.ImageUtil;
import org.pentaho.xul.swt.tab.TabItem;
import org.pentaho.xul.swt.tab.TabListener;
import org.pentaho.xul.swt.tab.TabSet;

/**
 * TODO: Move this into a base class, and have WebVisBrowser, etc extend it
 */
public class VisualizeCanvas implements TabItemInterface, ModifyListener, TabListener {

  private TabItem tabItem = null;
  boolean isTabSelected = false;
  private WebVisualizationBrowser browser;
  private IVisualization visualization;
  private String fileLocation;
  private String modelId;
    
  public VisualizeCanvas(IVisualization visualization, String fileLocation, String modelId) {
    this.visualization = visualization;
    this.fileLocation = fileLocation;
    this.modelId = modelId;
  }
  
  public void openVisualization() {
    if (visualization instanceof WebVisualization) {
      addVisualizationBrowser();
    }
  }
  
  private void addVisualizationBrowser() {
    WebVisualization webVis = (WebVisualization)visualization;
    
    // TODO: We'll need a better approach for tab name
    String tabName = modelId;
    
    Spoon spoon = ((Spoon)SpoonFactory.getInstance());
    TabSet tabfolder = spoon.tabfolder;
    
    try {
      // OK, now we have the HTML, create a new browse tab.

      // See if there already is a tab for this browser
      // If no, add it
      // If yes, select that tab
      //
      tabItem = spoon.delegates.tabs.findTabItem(tabName, TabMapEntry.OBJECT_TYPE_BROWSER);
      if (tabItem == null) {
        CTabFolder cTabFolder = tabfolder.getSwtTabset();
        browser = new WebVisualizationBrowser(cTabFolder, spoon, webVis, fileLocation, modelId);
        tabItem = new TabItem(tabfolder, tabName, tabName);
        Image visualizeTabImage = ImageUtil.getImageAsResource(spoon.getDisplay(), "plugins/spoon/agile-bi/ui/images/visualizer.png");
        tabItem.setImage(visualizeTabImage);
        tabItem.setControl(browser.getComposite());
        tabItem.addListener( this );
        tabfolder.addListener(this);

        spoon.delegates.tabs.addTab(new TabMapEntry(tabItem, tabName, browser, TabMapEntry.OBJECT_TYPE_BROWSER));
      }
      int idx = tabfolder.indexOf(tabItem);

      // keep the focus on the graph
      tabfolder.setSelected(idx);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
  public boolean canBeClosed() {
    // TODO Auto-generated method stub
    return true;
  }

  public Object getManagedObject() {
    // TODO Auto-generated method stub
    return null;
  }

  public EngineMetaInterface getMeta() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean hasContentChanged() {
    // TODO Auto-generated method stub
    return false;
  }

  public void setControlStates() {
    // TODO Auto-generated method stub
    
  }

  public int showChangedWarning() {
    // TODO Auto-generated method stub
    return 0;
  }

  public boolean applyChanges() {
    // TODO Auto-generated method stub
    return false;
  }

  public void setVisualization(IVisualization visualization) {
    this.visualization = visualization;
  }

  public void modifyText(ModifyEvent arg0) {
    // TODO Auto-generated method stub
    
  }

  public static void quickVisualize( DatabaseMeta databaseMeta, String modelName, String tableName, String sql ) {
    
    if (true) throw new UnsupportedOperationException();
    
    Database db = new Database( databaseMeta );
    RowMetaInterface rowMeta = null;
    try
    {
          db.connect();
          rowMeta = db.getQueryFields(sql, false);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    if( rowMeta != null ) {
      try {
        // TODO: incorporate into IVisualization API
        // VisualizeCanvas.openVisualizer(modelName, databaseMeta.getDatabaseName(), null);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void quickVisualize( TransMeta transMeta, StepMeta stepMeta ) {
    
    if (true) throw new UnsupportedOperationException();

    
    Spoon spoon = ((Spoon)SpoonFactory.getInstance());
//    TransMeta transMeta = spoon.getActiveTransformation();
    if( transMeta == null ) {
      SpoonFactory.getInstance().messageBox( "This must be open from a transformation", "Modeler", false, Const.ERROR);
      return;
    }
    StepMeta steps[] = transMeta.getSelectedSteps();
    if( steps == null || steps.length > 1 ) {
      SpoonFactory.getInstance().messageBox( "One (and only one) step must be selected", "Modeler", false, Const.ERROR);
      return;
    }
    
    // assume only one selected 
//    StepMeta stepMeta = steps[0];
    if( !(stepMeta.getStepMetaInterface() instanceof TableOutputMeta) ) {
      SpoonFactory.getInstance().messageBox( "A Table Output step must be selected", "Modeler", false, Const.ERROR);
      return;
    }
    RowMetaInterface rowMeta = null;
    try {
      rowMeta = transMeta.getStepFields(stepMeta);
    } catch (KettleException e) {
      e.printStackTrace();
      SpoonFactory.getInstance().messageBox( "Could not get transformation step metadata", "Modeler", false, Const.ERROR);
      return;
    }

    TableOutputMeta tableOutputMeta = (TableOutputMeta) stepMeta.getStepMetaInterface();
    DatabaseMeta databaseMeta = tableOutputMeta.getDatabaseMeta();
    String tableName = tableOutputMeta.getTablename();
    String modelName = stepMeta.getName();

    try {
      
      // TODO: Incorporate into IVisualization API
      // VisualizeCanvas.openVisualizer(modelName, databaseMeta.getDatabaseName(), null) ;
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  public boolean tabClose(TabItem item) {
    // TODO Auto-generated method stub
    return true;
  }

  public void tabDeselected(TabItem item) {
    System.out.println("tabDeselected");
    if( item == this.tabItem ) {
      
    }
  }

  public void tabSelected(TabItem item) {
    // TODO Auto-generated method stub
  }
  
}