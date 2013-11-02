package net.imglib2.vigra;

import io.scif.img.cell.cache.AbstractCacheService;
import io.scif.img.cell.cache.CacheResult;
import io.scif.img.cell.cache.CacheService;

import java.io.Serializable;

import org.scijava.plugin.Plugin;
import org.scijava.service.Service;

@Plugin(type = CacheService.class, priority = 1000)
public class DummyCacheService<T extends Serializable> extends AbstractCacheService<T> {
	public void clearCache(String cacheId) {
		throw new UnsupportedOperationException();
	}

	public void clearAllCaches() {
		throw new UnsupportedOperationException();
        }

	public void dropCache(String cacheId) {
		throw new UnsupportedOperationException();
        }

	public void addCache(String cacheId) {
		throw new UnsupportedOperationException();
        }

	public CacheResult cache(String cacheId, int index, T object) {
		throw new UnsupportedOperationException();
        }

	public T retrieve(String cacheId, int index) {
		throw new UnsupportedOperationException();
        }

	public T retrieveNoRecache(String cacheId, int index) {
		throw new UnsupportedOperationException();
        }

	public Integer getKey(String cacheId, int index) {
		throw new UnsupportedOperationException();
        }

	public void setMaxBytesOnDisk(long maxBytes) {
		throw new UnsupportedOperationException();
        }

	public boolean enabled() {
		throw new UnsupportedOperationException();
        }

	public void enable(boolean enabled) {
		throw new UnsupportedOperationException();
        }

	public void cacheAll(boolean enabled) {
		throw new UnsupportedOperationException();
        }
}

