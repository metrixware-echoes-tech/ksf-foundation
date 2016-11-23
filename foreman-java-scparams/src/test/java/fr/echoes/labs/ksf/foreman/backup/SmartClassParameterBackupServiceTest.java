package fr.echoes.labs.ksf.foreman.backup;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterWrapper;

public class SmartClassParameterBackupServiceTest {

	private static final String STORAGE_PATH = "build/tmp/dataset";
	private static final String SOURCE_PATH = "src/test/resources/dataset/";
	
	private final PuppetClass puppetClass1 = new PuppetClass(1, "class1", "module1");
	private final PuppetClass puppetClass2 = new PuppetClass(2, "class2", "module2");
	
	private BackupStorage backupStorage;
	private SmartClassParameterBackupService backupService;
	
	@Before
	public void setup() throws IOException {

		FileUtils.deleteDirectory(new File(STORAGE_PATH));
		new File(STORAGE_PATH).mkdirs();
		
		this.backupStorage = new BackupStorage(STORAGE_PATH);
		this.backupService = new SmartClassParameterBackupService(this.backupStorage);
	}
	
	private void initDataset() throws IOException {
		FileUtils.copyDirectory(new File(SOURCE_PATH), new File(STORAGE_PATH));
	}
	
	@Test
	public void testReadHostGroups() throws IOException {
		
		// given
		initDataset();
		
		// when
		final Map<String, List<SmartClassParameterWrapper>> results = this.backupService.readHostGroupValues();
		
		// then
		Assert.assertEquals(1, results.size());
		
		// then
		Entry<String, List<SmartClassParameterWrapper>> entry = results.entrySet().iterator().next();
		Assert.assertEquals("Provision from foreman.ksf.local", entry.getKey());
		Assert.assertEquals(17, entry.getValue().size());	
	}
	
	@Test
	public void testReadHost() throws IOException {
		
		// given
		initDataset();
		
		// and
		final ForemanHost host = new ForemanHost("1", "dashboard.ksf.local");
		
		// when
		final List<SmartClassParameterWrapper> results = this.backupService.read(host);
		
		// then
		Assert.assertEquals(14, results.size());
		
		// then
		final SmartClassParameterWrapper param = findSmartClassParameter(results, "java", "java", "package");
		Assert.assertNotNull(param);
		Assert.assertEquals("openjdk-8-jdk", param.getValue());
		Assert.assertEquals(false, param.getUsePuppetDefault());	
	}
	
	@Test
	public void testReadGlobalValues() throws IOException {
		
		// given
		initDataset();
		
		// when
		final List<SmartClassParameterWrapper> results = this.backupService.read();
		
		// then
		Assert.assertEquals(164, results.size());
		
		// then
		final SmartClassParameterWrapper param = findSmartClassParameter(results, "elasticsearch", "elasticsearch", "version");
		Assert.assertNotNull(param);
		Assert.assertEquals("2.3.4", param.getValue());
		Assert.assertEquals(false, param.getUsePuppetDefault());
	}
	
	@Test
	public void testWriteGlobalValues() throws IOException {
		
		// given
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(
				new SmartClassParameterWrapper("param1", "hash", puppetClass1, "value1", false),
				new SmartClassParameterWrapper("param2", "string", puppetClass1, null, true),
				new SmartClassParameterWrapper("param1", "hash", puppetClass2, "value3", false)
		);
		
		// when
		this.backupService.write(params);
		
		// then
		Assert.assertTrue(new File(STORAGE_PATH+"/puppet_classes_parameters.csv").exists());
		
		// then
		verifyWrittenValues(params, this.backupService.read());
		
	}
	
	@Test
	public void testWriteEmptyGlobalValues() throws IOException {
		
		// given
		final List<SmartClassParameterWrapper> params = Lists.newArrayList();
		
		// when
		this.backupService.write(params);
		
		// then
		verifyWrittenValues(params, this.backupService.read());
	}
	
	@Test
	public void testWriteHostGroupValues() throws IOException {
		
		// given
		final String hostGroup = "testHostGroup";
		
		// and
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(
				new SmartClassParameterWrapper("param1", "hash", puppetClass1, "value1", false),
				new SmartClassParameterWrapper("param2", "string", puppetClass1, null, true),
				new SmartClassParameterWrapper("param1", "hash", puppetClass2, "value3", false)
		);
		
		// when
		this.backupService.writeHostGroupValues(hostGroup, params);
		
		// then
		Assert.assertTrue(new File(STORAGE_PATH+"/hostgroups/"+hostGroup+"-parameters.csv").exists());
		
		// then
		final Map<String, List<SmartClassParameterWrapper>> hostGroupValues = this.backupService.readHostGroupValues();
		Assert.assertNotNull(hostGroupValues);
		Assert.assertEquals(1, hostGroupValues.size());
		
		// then
		verifyWrittenValues(params, hostGroupValues.get(hostGroup));
		
	}
	
	@Test
	public void testWriteHostGroupValuesWithSlash() throws IOException {
		
		// given
		final String hostGroup = "test Host /Group with Slash";
		
		// and
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(
				new SmartClassParameterWrapper("param1", "hash", puppetClass1, "value1", false),
				new SmartClassParameterWrapper("param2", "string", puppetClass1, null, true),
				new SmartClassParameterWrapper("param1", "hash", puppetClass2, "value3", false)
		);
		
		// when
		this.backupService.writeHostGroupValues(hostGroup, params);
		
		for (File file : new File(STORAGE_PATH+"/hostgroups/").listFiles()) {
			System.out.println(file.getName());
		}
		
		// then
		final Map<String, List<SmartClassParameterWrapper>> hostGroupValues = this.backupService.readHostGroupValues();
		System.out.println(hostGroupValues.keySet().iterator().next());
		Assert.assertNotNull(hostGroupValues);
		Assert.assertEquals(1, hostGroupValues.size());
		
		// then
		verifyWrittenValues(params, hostGroupValues.values().iterator().next());
	}
	
	@Test
	public void testWriteEmptyHostGroupValues() throws IOException {
		
		// given
		final String hostGroup = "testHostGroup";
		final List<SmartClassParameterWrapper> params = Lists.newArrayList();
		
		// when
		this.backupService.writeHostGroupValues(hostGroup, params);
		
		// then
		Assert.assertNull(this.backupService.readHostGroupValues().get(hostGroup));
	}
	
	@Test
	public void testWriteHostValues() throws IOException {
		
		// given
		final ForemanHost host = new ForemanHost("1", "test.ksf.local");
		
		// and
		final List<SmartClassParameterWrapper> params = Lists.newArrayList(
				new SmartClassParameterWrapper("param1", "hash", puppetClass1, "value1", false),
				new SmartClassParameterWrapper("param2", "string", puppetClass1, null, true),
				new SmartClassParameterWrapper("param1", "hash", puppetClass2, "value3", false)
		);
		
		// when
		this.backupService.write(host, params);
		
		// then
		Assert.assertTrue(new File(STORAGE_PATH+"/hosts/"+host.getName()+'/'+host.getName()+"-parameters.csv").exists());
		
		// then
		verifyWrittenValues(params, this.backupService.read(host));
		
	}
	
	@Test
	public void testWriteEmptyHostValues() throws IOException {
		
		// given
		final ForemanHost host = new ForemanHost("1", "test.ksf.local");
		final List<SmartClassParameterWrapper> params = Lists.newArrayList();
		
		// when
		this.backupService.write(host, params);
		
		// then
		verifyWrittenValues(params, this.backupService.read(host));
	}
	
	private static void verifyWrittenValues(final List<SmartClassParameterWrapper> expectedValues, final List<SmartClassParameterWrapper> writtenValues) {
		
		Assert.assertNotNull(writtenValues);
		Assert.assertEquals(expectedValues.size(), writtenValues.size());
		
		for (final SmartClassParameterWrapper param : expectedValues) {
			final SmartClassParameterWrapper writtenParam = findSmartClassParameter(writtenValues, param.getPuppetModule(), param.getPuppetClass(), param.getParameter());
			Assert.assertNotNull(writtenParam);
			Assert.assertEquals(param.getValue(), writtenParam.getValue());
			Assert.assertEquals(param.getUsePuppetDefault(), writtenParam.getUsePuppetDefault());
			Assert.assertEquals(param.getType(), writtenParam.getType());
		}		
	}
	
	private static SmartClassParameterWrapper findSmartClassParameter(final Collection<SmartClassParameterWrapper> params, final String module, final String puppetClass, final String parameterName) {
		
		final Collection<SmartClassParameterWrapper> matched = Collections2.filter(params, new Predicate<SmartClassParameterWrapper>() {
			@Override
			public boolean apply(final SmartClassParameterWrapper param) {
				return StringUtils.equals(module, param.getPuppetModule()) && StringUtils.equals(puppetClass, param.getPuppetClass()) && StringUtils.equals(parameterName, param.getParameter());
			}
		});
		
		return matched.isEmpty() ? null : matched.iterator().next();
	}
	
}
