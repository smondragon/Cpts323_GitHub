package groupProject;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.util.*;

import java.awt.*;
import java.io.InputStream;

/**
 * Illustrates how to use a World Wind <code>{@link Balloon}</code> to display on-screen information to the user in the
 * form of a screen-aligned text balloon. There are two abstract balloon types: <code>{@link ScreenBalloon}</code> which
 * displays a balloon at a point on the screen, and <code>{@link GlobeBalloon}</code> which displays a balloon attached
 * to a position on the Globe. For each abstract balloon type, there are two concrete types: AnnotationBalloon which
 * provides support for simple text content with an optional image, and BrowserBalloon which provides support for
 * complex HTML, JavaScript, and Flash content.
 * <p/>
 * <strong>Balloon Content</strong> <br/> A Balloon's content is specified by calling <code>{@link
 * Balloon#setText(String)}</code>, and its visual attributes are specified by calling <code>{@link
 * Balloon#setAttributes(gov.nasa.worldwind.render.BalloonAttributes)}</code> with an instance of <code>{@link
 * BalloonAttributes}</code>.
 * <p/>
 * <strong>ScreenBalloon</strong> <br/> ScreenBalloons display a screen-aligned balloon at a point on the screen. There
 * are two concrete ScreenBalloon types: <ul> <li><code>{@link ScreenAnnotationBalloon}</code> - a screen-attached
 * balloon with support for multi-line text, a background image, simple HTML text markup, and simple text styling
 * attributes.</li> <li><code>{@link ScreenBrowserBalloon}</code> - a screen-attached balloon with support for HTML,
 * JavaScript, and Flash content.</li> </ul>
 * <p/>
 * <strong>GlobeBalloon</strong> <br/> GlobeBalloons display a screen-aligned balloon attached to a position on the
 * Globe. <ul> <li><code>{@link GlobeAnnotationBalloon}</code> - a Globe-attached balloon with support for multi-line
 * text, a background image, simple HTML text markup, and simple text styling attributes.</li> <li><code>{@link
 * GlobeBrowserBalloon}</code> - a Globe-attached balloon with support for HTML, JavaScript, and Flash content.</li>
 * </ul>
 *
 * @author pabercrombie
 * @version $Id: Balloons.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Balloons extends ApplicationTemplate
{
    protected static final String BROWSER_BALLOON_CONTENT_PATH
        = "gov/nasa/worldwindx/examples/data/BrowserBalloonExample.html";

    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        protected HotSpotController hotSpotController;
        protected BalloonController balloonController;
        protected RenderableLayer layer;

        public AppFrame()
        {
            super(true, true, false);

            // Add a controller to send input events to BrowserBalloons.
            this.hotSpotController = new HotSpotController(this.getWwd());
            // Add a controller to handle link and navigation events in BrowserBalloons.
            this.balloonController = new BalloonController(this.getWwd());

            // Create a layer to display the balloons.
            this.layer = new RenderableLayer();
            this.layer.setName("Balloons");
            insertBeforePlacenames(getWwd(), this.layer);
            this.getLayerPanel().update(this.getWwd());

            // Add an AnnotationBalloon and a BrowserBalloon to the balloon layer.
           
  // ------------------------------------------     BELOW THIS ---------------------------------------
            
            // Below is dummy data. Needs to get data from an actual alert.
            Alert alert = new Alert();
            alert.setPoint1(38.5);
            alert.setPoint2(-87.223);
            
            // The main issue here is going through the list of alerts and creating multiple balloons.
            // I dunno how to do that.
            this.makeBrowserBalloon(alert);

            
  // ---------------------------------------------	ABOVE THIS ----------------------------------------
            
            
            // Size the World Window to provide enough screen space for the BrowserBalloon, and center the World Window
            // on the screen.
            Dimension size = new Dimension(1200, 800);
            this.setPreferredSize(size);
            this.pack();
            WWUtil.alignComponent(null, this, AVKey.CENTER);
        }
        
        protected void makeBrowserBalloon(Alert alert)
        {
            String htmlString = null;
            InputStream contentStream = null;

            try
            {
                // Read the URL content into a String using the default encoding (UTF-8).
                contentStream = WWIO.openFileOrResourceStream(BROWSER_BALLOON_CONTENT_PATH, this.getClass());
                htmlString = WWIO.readStreamToString(contentStream, null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                WWIO.closeStream(contentStream, BROWSER_BALLOON_CONTENT_PATH);
            }

            if (htmlString == null)
                htmlString = Logging.getMessage("generic.ExceptionAttemptingToReadFile", BROWSER_BALLOON_CONTENT_PATH);

            
            
            Position balloonPosition = Position.fromDegrees(alert.getPoint1(), alert.getPoint2());

            // Create a Browser Balloon attached to the globe, and pointing at the NASA headquarters in Washington, D.C.
            // We use the balloon page's URL as its resource resolver to handle relative paths in the page content.
            AbstractBrowserBalloon balloon = new GlobeBrowserBalloon(htmlString, balloonPosition);
            // Size the balloon to provide enough space for its content.
            BalloonAttributes attrs = new BasicBalloonAttributes();
            attrs.setSize(new Size(Size.NATIVE_DIMENSION, 0d, null, Size.NATIVE_DIMENSION, 0d, null));
            balloon.setAttributes(attrs);

            // Create a placemark on the globe that the user can click to open the balloon.
            PointPlacemark placemark = new PointPlacemark(balloonPosition);
            placemark.setLabelText("Click to open balloon");
            // Associate the balloon with the placemark by setting AVKey.BALLOON. The BalloonController looks for this
            // value when an object is clicked.
            placemark.setValue(AVKey.BALLOON, balloon);

            this.layer.addRenderable(balloon);
            this.layer.addRenderable(placemark);
        }
    }

    public static void main(String[] args)
    {
        // Configure the initial view parameters so that the browser balloon is centered in the viewport.
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 35);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -87);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 9500000);
        Configuration.setValue(AVKey.INITIAL_PITCH, 45);

        ApplicationTemplate.start("World Wind Balloons", AppFrame.class);
        
        
        
        
    }
}

