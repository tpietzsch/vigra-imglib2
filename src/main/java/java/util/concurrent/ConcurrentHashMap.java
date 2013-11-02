package java.util.concurrent;

import java.util.HashMap;

/**
 * A dummy wrapper around {@link HashMap}.
 * <p>
 * Avian VM's default class path does not have concurrent hash maps. Hopefully
 * we won't need them as far as SCIFIO is concerned, so let's just punt for
 * the moment.
 * </p>
 * @author Johannes Schindelin
 */
public class ConcurrentHashMap extends HashMap {}
