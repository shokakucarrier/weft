package org.commonjava.cdi.util.weft.config;

import java.util.HashMap;
import java.util.Map;

public class DefaultWeftConfig
    implements WeftConfig
{

    public static final String THREADS_SUFFIX = "t";

    public static final String PRIORITY_SUFFIX = "p";

    private static final int DEFAULT_THREADS = Runtime.getRuntime()
                                                      .availableProcessors() * 2;

    private static final int DEFAULT_PRIORITY = 8;

    private final Map<String, Integer> config = new HashMap<>();

    private int defaultThreads = DEFAULT_THREADS;

    private int defaultPriority = DEFAULT_PRIORITY;

    public DefaultWeftConfig()
    {
    }

    public DefaultWeftConfig( final Map<String, Integer> config )
    {
        this.config.putAll( config );
    }

    public DefaultWeftConfig( final DefaultWeftConfig config )
    {
        this.config.putAll( config.config );
    }

    public DefaultWeftConfig configureDefaultThreads( final int defaultThreads )
    {
        this.defaultThreads = defaultThreads;
        return this;
    }

    public DefaultWeftConfig configureDefaultPriority( final int defaultPriority )
    {
        this.defaultPriority = defaultPriority;
        return this;
    }

    public DefaultWeftConfig configurePool( final String name, final int threads, final int priority )
    {
        config.put( name + THREADS_SUFFIX, threads );
        config.put( name + PRIORITY_SUFFIX, priority );

        return this;
    }

    public DefaultWeftConfig configureThreads( final String name, final int threads )
    {
        config.put( name + THREADS_SUFFIX, threads );

        return this;
    }

    public DefaultWeftConfig configurePriority( final String name, final int priority )
    {
        config.put( name + PRIORITY_SUFFIX, priority );

        return this;
    }

    @Override
    public int getDefaultThreads()
    {
        return defaultThreads;
    }

    @Override
    public int getDefaultPriority()
    {
        return defaultPriority;
    }

    @Override
    public int getThreads( final String poolName )
    {
        return getWithDefaultAndFailover( poolName, THREADS_SUFFIX, null, getDefaultThreads() );
    }

    @Override
    public int getThreads( final String poolName, final Integer defaultValue )
    {
        return getWithDefaultAndFailover( poolName, THREADS_SUFFIX, defaultValue, getDefaultThreads() );
    }

    @Override
    public int getPriority( final String poolName )
    {
        return getWithDefaultAndFailover( poolName, PRIORITY_SUFFIX, null, getDefaultPriority() );
    }

    @Override
    public int getPriority( final String poolName, final Integer defaultValue )
    {
        return getWithDefaultAndFailover( poolName, PRIORITY_SUFFIX, defaultValue, getDefaultPriority() );
    }

    private int getWithDefaultAndFailover( final String poolName, final String suffix, final Integer defaultValue, final int failover )
    {
        final Integer v = config.get( poolName + suffix );
        if ( v == null )
        {
            return defaultValue == null ? failover : defaultValue;
        }

        return v;
    }

}