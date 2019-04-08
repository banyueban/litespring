package org.litespring.core.type.classreading;

import org.litespring.util.ClassUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;

/**
 * @author Charis
 * asm框架读取字节码文件,重写ClassVisitor的visitor方法
 */
public class ClassMetadataReadingVisitor extends ClassVisitor {

	private String className;
	
	private String superClassName;
	
	private boolean isAbstract;
	
	private boolean isInterface;
	
	private boolean isFinal;
	
	private String[] interfaces;
	
	public ClassMetadataReadingVisitor() {
		super(SpringAsmInfo.ASM_VERSION);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
		this.className = ClassUtils.convertResourcePathToClassName(name);
		this.isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
		this.isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
		this.isFinal = (access & Opcodes.ACC_FINAL) != 0;
		if (supername != null) {
			this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
		}
		this.interfaces = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
		}
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public boolean isInterface() {
		return this.isInterface;
	}

	public boolean isFinal() {
		return this.isFinal;
	}

	public boolean isConcrete() {
		return !(this.isInterface || this.isAbstract);
	}
	public String getClassName() {
		return this.className;
	}
	
	public boolean hasSuperClass() {
		return this.superClassName != null;
	}
	
	public String getSuperClassName() {
		return this.superClassName;
	}

	public String[] getInterfaceNames() {
		return this.interfaces;
	}
	
	
}
