package fr.echoes.lab.foremanapi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamGobbler extends Thread {

	final static Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
	private final InputStream is;
	private final List<String> outputLines = new ArrayList<String>();
    private String debugDisplayName;

	StreamGobbler(final String name, final InputStream is) {
	    super(name);
		this.is = is;
	}

    /**
     * TODO comment for getDebugDisplayName.
     * @return
     */
    String getDebugDisplayName() {
        if (this.debugDisplayName == null) {
            this.debugDisplayName = getName() + getId();
        }
        return this.debugDisplayName;
    }

	@Override
	public void run() {
		try {
			final InputStreamReader isr = new InputStreamReader(this.is);
			final BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.debug("StreamGobbler " + getDebugDisplayName() + " received line: " + line);  //$NON-NLS-1$//$NON-NLS-2$
				this.outputLines.add(line);
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public List<String> getStreamOutputLines() {
		return this.outputLines;
	}
}