package org.springframework.boot.configurationprocessor.metadata;

/**
 * @author qiubo
 */

public class ItemMetadata implements Comparable<ItemMetadata> {
	
	private ItemMetadata.ItemType itemType;
	
	private String name;
	
	private String type;
	
	private String description;
	
	private String sourceType;
	
	private String sourceMethod;
	
	private Object defaultValue;
	
	private ItemDeprecation deprecation;
	
	ItemMetadata(	ItemMetadata.ItemType itemType, String prefix, String name, String type, String sourceType,
					String sourceMethod, String description, Object defaultValue, ItemDeprecation deprecation) {
		super();
		this.itemType = itemType;
		this.name = buildName(prefix, name);
		this.type = type;
		this.sourceType = sourceType;
		this.sourceMethod = sourceMethod;
		this.description = description;
		this.defaultValue = defaultValue;
		this.deprecation = deprecation;
	}
	
	private String buildName(String prefix, String name) {
		while (prefix != null && prefix.endsWith(".")) {
			prefix = prefix.substring(0, prefix.length() - 1);
		}
		StringBuilder fullName = new StringBuilder(prefix == null ? "" : prefix);
		if (fullName.length() > 0 && name != null) {
			fullName.append(".");
		}
		fullName.append(name == null ? "" : name);
		return fullName.toString();
	}
	
	public boolean isOfItemType(ItemMetadata.ItemType itemType) {
		return this.itemType == itemType;
	}
	
	public boolean hasSameType(ItemMetadata metadata) {
		return this.itemType == metadata.itemType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSourceType() {
		return this.sourceType;
	}
	
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	public String getSourceMethod() {
		return this.sourceMethod;
	}
	
	public void setSourceMethod(String sourceMethod) {
		this.sourceMethod = sourceMethod;
	}
	
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public ItemDeprecation getDeprecation() {
		return this.deprecation;
	}
	
	public void setDeprecation(ItemDeprecation deprecation) {
		this.deprecation = deprecation;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder(this.name);
		buildToStringProperty(string, "type", this.type);
		buildToStringProperty(string, "sourceType", this.sourceType);
		buildToStringProperty(string, "description", this.description);
		buildToStringProperty(string, "defaultValue", this.defaultValue);
		buildToStringProperty(string, "deprecation", this.deprecation);
		return string.toString();
	}
	
	protected final void buildToStringProperty(StringBuilder string, String property, Object value) {
		if (value != null) {
			string.append(" ").append(property).append(":").append(value);
		}
	}
	
	@Override
	public int compareTo(ItemMetadata o) {
		return getName().compareTo(o.getName());
	}
	
	public static ItemMetadata newGroup(String name, String type, String sourceType, String sourceMethod) {
		return new ItemMetadata(ItemMetadata.ItemType.GROUP, name, null, type, sourceType, sourceMethod, null, null,
			null);
	}
	
	public static ItemMetadata newProperty(	String prefix, String name, String type, String sourceType,
											String sourceMethod, String description, Object defaultValue,
											ItemDeprecation deprecation) {
		return new ItemMetadata(ItemMetadata.ItemType.PROPERTY, prefix, name, type, sourceType, sourceMethod,
			description, defaultValue, deprecation);
	}
	
	/**
	 * The item type.
	 */
	public enum ItemType {
							
							/**
							 * Group item type.
							 */
							GROUP,
							
							/**
							 * Property item type.
							 */
							PROPERTY
							
	}
	
}
