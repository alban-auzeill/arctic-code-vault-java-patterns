package php.runtime.memory.support.operation; // (rank 501) copied from https://github.com/jphp-group/jphp/blob/eefaac6d8195025ba53c2b7b0996a9c128b2ffb7/jphp-runtime/src/php/runtime/memory/support/operation/PatternMemoryOperation.java

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.util.WrapRegex;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

import java.util.regex.Pattern;

public class PatternMemoryOperation extends MemoryOperation<Pattern> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Pattern.class};
    }

    @Override
    public Pattern convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.instanceOf(WrapRegex.class)) {
            return arg.toObject(WrapRegex.class).getMatcher().pattern();
        } else {
            return Pattern.compile(arg.toString());
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Pattern arg) throws Throwable {
        return ObjectMemory.valueOf(new WrapRegex(env, arg.matcher(""), ""));
    }
}
