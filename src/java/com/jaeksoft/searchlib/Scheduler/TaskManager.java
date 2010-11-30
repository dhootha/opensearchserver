/**   
 * License Agreement for Jaeksoft OpenSearchServer
 *
 * Copyright (C) 2010 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of Jaeksoft OpenSearchServer.
 *
 * Jaeksoft OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Jaeksoft OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jaeksoft OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.Scheduler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.jaeksoft.searchlib.SearchLibException;

public class TaskManager {

	private final static Lock lock = new ReentrantLock();

	private static Scheduler scheduler = null;

	public static void start() throws SearchLibException {
		try {
			lock.lock();
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			throw new SearchLibException(e);
		} finally {
			lock.unlock();
		}
	}

	public static void stop() throws SearchLibException {
		try {
			lock.lock();
			if (scheduler != null)
				scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new SearchLibException(e);
		} finally {
			lock.unlock();
		}
	}
}
