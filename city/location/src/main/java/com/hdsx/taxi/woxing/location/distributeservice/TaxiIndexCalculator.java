package com.hdsx.taxi.woxing.location.distributeservice;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdsx.taxi.woxing.bean.TaxiIndex;
import com.hdsx.taxi.woxing.location.util.Config;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 打车指数计算
 * 
 * @author Steven
 * 
 */
public class TaxiIndexCalculator {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(TaxiIndexCalculator.class);

;

	static GeometryFactory geof;
	static PrecisionModel intPreMode = new PrecisionModel(1000000);

	final static int[] levels = new int[] { Integer.MAX_VALUE, 10000, 8000,
			5000, 1000, Integer.MIN_VALUE }; // 打车指数界定

	int xcount, ycount;

	List<int[][]> list = new ArrayList<int[][]>();

	Position start = new Position();
	List<TaxiIndex> result = new ArrayList<TaxiIndex>();
	static int[][] dir = new int[][] { { -1, 0 }, { -1, -1 }, { 0, -1 },
			{ 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 },
			{ -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 },
			{ -1, 1 } };

	private float[][] matrix;

	double xmin, ymin, cell;

	public TaxiIndexCalculator(float[][] matrix, double xmin, double ymin,
			double cell) {

		this.xcount = matrix[0].length;
		this.ycount = matrix.length;
		this.matrix = matrix;

		// this.xcount = matrix[0].length + 2;
		// this.ycount = matrix.length + 2;
		//
		// this.matrix = new float[ycount][xcount];

		// for (int i = 1; i < ycount - 1; i++)
		// for (int j = 1; j < xcount - 1; j++)
		// this.matrix[i][j] = matrix[i - 1][j - 1];

		this.xmin = xmin - cell / 2;
		this.ymin = ymin - cell - cell / 2;
		this.cell = cell;
		if (geof == null)
			geof = new GeometryFactory(intPreMode);
		// toBinary2();
	}

	/**
	 * 将输入矩阵格式化为不同的打车级别
	 */
	void toBinary() {
		for (int level = 0; level < levels.length - 1; level++) {
			int[][] int_matrix = new int[ycount][xcount];
			for (int i = start.getX(); i < xcount; i++) {
				for (int j = start.getY() + 1; j < ycount; j++) {
					float v = matrix[j][i];
					if (v > levels[level]) {
						int_matrix[j][i] = 1;
						matrix[j][i] = 0;
					} else {
						int_matrix[j][i] = 0;
					}

				}
			}
			this.list.add(int_matrix);
		}
	}

	void toBinary2() {
		long now = System.currentTimeMillis();

		for (int n = 1; n < Config.TAXI_INDEX_ARGS.size(); n++) {
			TaxiIndexArgments tig = Config.TAXI_INDEX_ARGS.get(n);
			// for (TaxiIndexArgments tig : Config.TAXI_INDEX_ARGS) {
			int[][] int_matrix = new int[ycount][xcount];
			for (int i = start.getX(); i < xcount; i++) {
				for (int j = start.getY() + 1; j < ycount; j++) {
					float v = matrix[j][i];
					if (v > tig.getMinvalue()) {
						int_matrix[j][i] = 1;
						// matrix[j][i] = 0;
					} else {
						int_matrix[j][i] = 0;
					}

				}
			}
			this.list.add(int_matrix);
		}
		now = System.currentTimeMillis() - now;
		if (logger.isDebugEnabled())
			logger.debug("To Binary花费时间：" + now);
	}

	/**
	 * 计算打车指数
	 * 
	 * @return
	 */
	public List<TaxiIndex> getAggreationArea2() {
		byte index = 1;

		for (int[][] intmatrix : this.list) {
			TaxiIndex ti = new TaxiIndex();

			ti.setIndex((byte) (index + 1));
			ti.setWaittime(Config.TAXI_INDEX_ARGS.get(index).getWaittime());
			boolean isOkey = false;
			while (!isOkey) {
				isOkey = getArea(intmatrix, ti);
			}

			// List<Polygon> poly = getArea(intmatrix);
			// ti.addAllPolygon(poly);

			result.add(ti);
			index++;
		}

		return result;
	}

	public List<TaxiIndex> getAggreationArea() {
		byte index = 2;
		for (int n = index; n < Config.TAXI_INDEX_ARGS.size(); n++) {
			TaxiIndexArgments tig = Config.TAXI_INDEX_ARGS.get(n);

			TaxiIndex ti = new TaxiIndex();

			ti.setIndex((byte) (index + 1));
			ti.setWaittime(tig.getWaittime());
			// boolean isOkey = false;
			// while (!isOkey) {
			// isOkey = getArea(intmatrix, ti);
			// }

			List<Polygon> poly = getArea(tig.getMinvalue());
			ti.addAllPolygon(poly);

			result.add(ti);
			index++;
		}

		return result;
	}

	boolean getArea(int[][] intMatrix, TaxiIndex ti) {

		boolean isComplete = true;
		Polygon poly = null;

		List<Position> lst = new ArrayList<Position>();
		// 找起始点

		OK: for (int i = 0; i < this.ycount; i++) {
			for (int j = 0; j < this.xcount; j++) {
				if (intMatrix[i][j] == 1) {
					start.setX(j);
					start.setY(i);
					isComplete = false;
					// logger.debug("找到起始点：" + start.toString());
					break OK;
				}
			}
		}
		if (isComplete)
			return true;

		Position curPosition = start;
		lst.add(start);
		int count = 0;
		do {

			curPosition = findNextEdge(curPosition, intMatrix);

			if (curPosition != null) {
				lst.add(curPosition);
			} else {
				break;
			}
			count++;
		} while (!curPosition.equals(start) && count < 100000);
		if (lst.size() > 3) {
			poly = toPolygon2(lst);

			// lstPolygon.add(poly);
			// erase(poly, lst, intMatrix);
			erase(lst, intMatrix);

			// Coordinate[] cors = poly.getCoordinates();
			// DouglasPeuckerLineSimplifier a;

			// poly = (Polygon) DouglasPeuckerSimplifier.simplify(poly, cell /
			// 2);
			if (poly != null) {
				ti.addPolygon(poly);
				if (logger.isDebugEnabled()) {
					logger.debug(poly.toString());
				}
			}

		} else {
			for (Position pos : lst) {
				erase(pos.getX(), pos.getY(), intMatrix);
			}
		}
		return false;

	}

	/**
	 * 识别多边形
	 * 
	 * @param minvalue
	 * @return
	 */
	List<Polygon> getArea(double minvalue) {

		List<Polygon> result = new ArrayList<>();

		// 找起始点

		List<Position> lst_bound = new ArrayList<Position>();

		for (int i = 0; i < this.ycount; i++) {
			boolean preIsbiger = false;
			for (int j = 1; j < this.xcount; j++) {
				boolean thisIsbier = this.matrix[i][j] > minvalue;
				if (preIsbiger == false && thisIsbier) {
					Position p = new Position();
					p.setX(j);
					p.setY(i);
					if (!lst_bound.contains(p)) {
						start.setX(j);
						start.setY(i);
						Polygon poly = null;
						// logger.debug("找到起始点：" + start.toString());
						List<Position> lst = new ArrayList<Position>();
						Position curPosition = start;
						lst.add(start);
						int count = 0;
						do {
							curPosition = findNextEdge(curPosition, minvalue);
							if (curPosition != null) {
								lst.add(curPosition);
								lst_bound.add(curPosition);
							} else {
								break;
							}
							count++;
						} while (!curPosition.equals(start) && count < 100000);

						if (lst.size() > 3) {

							List<Polygon> polygonlist = toPolygonList(lst);
							result.addAll(polygonlist);
							// // poly = toPolygon2(lst);
							// if (poly != null) {
							// result.add(poly);
							// // if (logger.isDebugEnabled()) {
							// // logger.debug(poly);
							// // }
							// }
						}
					}
				}
				preIsbiger = thisIsbier;
			}
		}

		return result;

	}

	/**
	 * 识别多边形，包括内边界
	 * 
	 * @param minvalue
	 * @return
	 */
	List<Polygon> getArea2(double minvalue) {

		List<Polygon> result = new ArrayList<>();

		// 找起始点

		List<Position> lst_bound = new ArrayList<Position>();

		for (int i = 0; i < this.ycount; i++) {
			boolean preIsbiger = false;
			for (int j = 1; j < this.xcount; j++) {
				boolean thisIsbier = this.matrix[i][j] > minvalue;
				if (preIsbiger == false && thisIsbier) {
					Position p = new Position();
					p.setX(j);
					p.setY(i);
					if (!lst_bound.contains(p)) {
						start.setX(j);
						start.setY(i);
						Polygon poly = null;
						// logger.debug("找到起始点：" + start.toString());
						List<Position> lst = new ArrayList<Position>();

						List<List<Position>> lst_holl = new ArrayList<>();
						Position curPosition = start;
						lst.add(start);
						int count = 0;
						do {
							curPosition = findNextEdge(curPosition, minvalue);
							if (curPosition != null) {
								lst.add(curPosition);
								lst_bound.add(curPosition);
							} else {
								break;
							}
							count++;
						} while (!curPosition.equals(start) && count < 100000);
						// 开始识别内边界
						if (lst.size() > 3) {
							for (Position boundpnt : lst) {
								int dir = boundpnt.getDir() % 8;
								if (dir > 0 && dir < 4) // 向右扫描，所以必须要向上方向才有内部结构
								{
									Position nextposition = new Position();
									nextposition.setX(boundpnt.getX() + 1);
									nextposition.setY(boundpnt.getY());

									boolean preIsbiger_inside = true;
									for (int xstart = boundpnt.getX() + 1; xstart < this.xcount; xstart++) {
										if (lst_bound.contains(nextposition))
											break;
										boolean thisIsbier_inside = this.matrix[i][j] > minvalue;
										if (preIsbiger_inside == false
												&& thisIsbier_inside) {
											Position pinside = new Position();
											pinside.setX(j);
											pinside.setY(i);
											if (!lst_bound.contains(pinside)) {
												start.setX(j);
												start.setY(i);

												// logger.debug("找到起始点：" +
												// start.toString());
												List<Position> lst_inside = new ArrayList<Position>();
												Position curPosition_inside = start;
												lst_inside.add(start);
												int count_inside = 0;
												do {
													curPosition_inside = findNextEdge(
															curPosition_inside,
															minvalue);
													if (curPosition_inside != null) {
														lst_inside
																.add(curPosition_inside);
														lst_bound
																.add(curPosition);
													} else {
														break;
													}
													count_inside++;
												} while (!curPosition_inside
														.equals(start)
														&& count_inside < 100000);

												if (lst_inside.size() > 3) {
													lst_holl.add(lst_inside);
												}
											}
										}
										preIsbiger_inside = thisIsbier_inside;
									}

								}

							}
						}

						if (lst.size() > 3) {
							poly = toPolygon2(lst);
							if (poly != null) {
								result.add(poly);
								// if (logger.isDebugEnabled()) {
								// logger.debug(poly);
								// }
							}
						}
					}
				}
				preIsbiger = thisIsbier;
			}
		}

		return result;

	}

	/**
	 * 整形的情况
	 * 
	 * @param intMatrix
	 * @return
	 */
	List<Polygon> getArea(int[][] intMatrix) {

		List<Polygon> result = new ArrayList<>();

		// 找起始点

		List<Position> lst_bound = new ArrayList<Position>();

		for (int i = 0; i < this.ycount; i++) {
			int prevalue = 0;
			for (int j = 1; j < this.xcount; j++) {
				Position p = new Position();
				p.setX(j);
				p.setY(i);

				if (prevalue == 0 && intMatrix[i][j] == 1)
					if (!lst_bound.contains(p)) {
						start.setX(j);
						start.setY(i);
						Polygon poly = null;
						// logger.debug("找到起始点：" + start.toString());
						List<Position> lst = new ArrayList<Position>();
						Position curPosition = start;
						lst.add(start);
						int count = 0;
						do {

							curPosition = findNextEdge(curPosition, intMatrix);

							if (curPosition != null) {
								lst.add(curPosition);
								lst_bound.add(curPosition);
							} else {
								break;
							}
							count++;
						} while (!curPosition.equals(start) && count < 100000);
						if (lst.size() > 3) {
							poly = toPolygon2(lst);
							if (poly != null) {
								result.add(poly);
								if (logger.isDebugEnabled()) {
									logger.debug(poly.toString());
								}
							}

						}

					}
				prevalue = intMatrix[i][j];
			}
		}

		return result;

	}

	Coordinate tranCoor(Position p) {
		return tranCoor(p.getX(), p.getY());
	}

	Coordinate tranCoor(int x, int y) {

		double dx = this.xmin + x * this.cell;
		double dy = this.ymin + (this.ycount - y) * this.cell;
		return new Coordinate(dx, dy);
	}

	void erase(int x, int y, int[][] intmatrix) {
		intmatrix[y][x] = 0;
	}

	/**
	 * 擦除已经识别出来的范围
	 * 
	 * @param intMatrix
	 */
	void erase(Polygon p, List<Position> lst, int[][] intMatrix) {

		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		for (Position c : lst) {
			xmin = xmin > c.x ? c.x : xmin;
			ymin = ymin > c.y ? c.y : ymin;
			xmax = xmax > c.x ? xmax : c.x;
			ymax = ymax > c.y ? ymax : c.y;
		}

		for (int i = xmin - 1; i <= xmax + 1; i++) {
			for (int j = ymin - 1; j <= ymax + 1; j++) {
				Point pnt = geof.createPoint(tranCoor(i, j));
				if (p.intersects(pnt))
					intMatrix[j][i] = 0;
			}
		}

		// logger.debug("完成擦除");

	}

	void erase2(List<Position> lst, int[][] intMatrix) {
		for (Position p : lst) {

		}
	}

	void erase(List<Position> lst, int[][] intMatrix) {
		for (Position p : lst) {
			int dir = 1;

			int x = p.getX(), y = p.getY();

			int v = intMatrix[y][x];
			if (x == 0)
				dir = 1;
			else if (x == this.xcount - 1)
				dir = -1;
			else if (intMatrix[y][x + 1] == 1)
				dir = 1;
			else if (intMatrix[y][x - 1] == 1)
				dir = -1;

			if (v == 1) {
				while (v == 1) {
					intMatrix[y][x] = 0;
					x = x + dir;
					v = intMatrix[y][x];
				}
			}
		}

	}

	private List<Polygon> toPolygonList(List<Position> lst) {
		List<Polygon> result = new ArrayList<Polygon>();
		List<Position> todolist = new ArrayList<Position>();
		for (int i = 0; i < lst.size(); i++) {

			Position p = lst.get(i);
			int idx = todolist.indexOf(p);
			if (idx > 0) {

				List<Position> tlist = new ArrayList<Position>();

				for (int n = idx; n < todolist.size(); n++)
					tlist.add(todolist.get(n));
				tlist.add(p);

				Polygon polygon = toPolygon2(tlist);
				if (polygon != null)
					result.add(polygon);

				todolist.removeAll(tlist);
				todolist.add(p);

			} else
				todolist.add(p);
		}

		if (todolist.size() > 3) {
			Polygon polygon = toPolygon2(todolist);
			if (polygon != null)
				result.add(polygon);
		}

		return result;

	}

	private Polygon toPolygon2(List<Position> lst) {

		if (!Config.TAXI_DISTRIBUTE_REDUCED)

			return toPolygon(lst);

		ArrayList<Coordinate> corlist = new ArrayList<Coordinate>();

		int dx = 0, dy = 0;
		corlist.add(tranCoor(lst.get(0)));

		dx = lst.get(1).getX() - lst.get(0).getX();
		dy = lst.get(1).getY() - lst.get(0).getY();
		for (int i = 1; i < lst.size() - 1; i++) {
			Position pos1 = lst.get(i);
			Position pos2 = lst.get(i + 1);
			int dx1 = pos2.getX() - pos1.getX();
			int dy1 = pos2.getY() - pos1.getY();
			if (dx1 != dx || dy1 != dy) {
				Coordinate cor = tranCoor(pos1);

				corlist.add(cor);
			}
			dx = dx1;
			dy = dy1;
		}
		corlist.add(tranCoor(lst.get(lst.size() - 1)));
		Coordinate[] cors = corlist.toArray(new Coordinate[1]);

		Polygon p = null;
		try {
			if (cors.length > 3) {
				p = geof.createPolygon(geof.createLinearRing(cors), null);
				if (!p.isValid()) {
					logger.error("有不符合要求的polygon出现" + p.toString());

					// Geometry p1 = p.buffer(0);
					// if(p1.getGeometryType().equalsIgnoreCase("MultiPolygon"))
					// {
					// MultiPolygon mp=(MultiPolygon) p1;
					//
					//
					// }
					// else
					// p=(Polygon) p1;
				}
			}

			// logger.debug("转换为Polygon" + p.toString());
		} catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage());
			return null;
		}

		// p = (Polygon) DouglasPeuckerSimplifier.simplify(p, this.cell / 2);

		return p;
	}

	/**
	 * 将识别出来的范围转换为Polygon对象
	 * 
	 * @param lst
	 * @return
	 */
	private Polygon toPolygon(List<Position> lst) {

		Coordinate[] cors = new Coordinate[lst.size()];
		for (int i = 0; i < lst.size(); i++) {
			Position pos = lst.get(i);

			int x, y;
			// x = pos.getDir() < 4 ? pos.getX() - 1 : pos.getX();
			x = pos.getX();
			// y = pos.getDir() > 3 ? pos.getY() + 1 : pos.getY();
			y = pos.getY();
			cors[i] = tranCoor(x, y);
		}
		// LinearRing lr = geof.createLinearRing(cors);
		// logger.debug("转换为LinearRing" + lr.toString());
		Polygon p = null;
		try {
			p = geof.createPolygon(geof.createLinearRing(cors), null);
			// logger.debug("转换为Polygon" + p.toString());
		} catch (IllegalArgumentException ex) {
			logger.error(ex.getMessage());
			return null;
		}

		// p = (Polygon) DouglasPeuckerSimplifier.simplify(p, this.cell / 2);

		return p;
	}

	/**
	 * 找到第一个为真的位置
	 * 
	 * @param curPos
	 * @return
	 */
	Position findNextEdge(Position curPos, int[][] intmatrix) {
		Position next = null;
		int startIndex = getNextStart(curPos.getDir());
		int x = curPos.getX();
		int y = curPos.getY();

		for (int i = startIndex; i < 16; i++) {
			int[] offset = dir[i];
			int cur_x = x + offset[0];
			int cur_y = y + offset[1];
			if (intmatrix[cur_y][cur_x] == 1) {
				next = new Position();
				next.setX(cur_x);
				next.setY(cur_y);
				next.setDir(i);
				// logger.debug("找到下一个点:" + next.toString());
				break;
			}

		}
		return next;

	}

	/**
	 * 找到第一个为真的位置 直接利用float查询
	 * 
	 * @param curPos
	 * @param minvalue
	 * @return
	 */
	Position findNextEdge(Position curPos, double minvalue) {
		Position next = null;
		int startIndex = getNextStart(curPos.getDir());
		int x = curPos.getX();
		int y = curPos.getY();

		for (int i = startIndex; i < 16; i++) {
			int[] offset = dir[i];
			int cur_x = x + offset[0];
			int cur_y = y + offset[1];
			if (this.matrix[cur_y][cur_x] > minvalue) {
				next = new Position();
				next.setX(cur_x);
				next.setY(cur_y);
				next.setDir(i);
				// logger.debug("找到下一个点:" + next.toString());
				break;
			}

		}
		return next;

	}

	// 找到下一個
	int getNextStart(int last) {
		if (last < 0)
			return 0;
		int v = last > 8 ? last - 8 : last;
		v += 5;
		v = v > 8 ? v - 8 : v;
		return v;
	}

	/**
	 * 内部类，记录起点位置
	 * 
	 * @author Steven
	 * 
	 */
	class Position extends Coordinate {

		/**
		 * 
		 */
		private static final long serialVersionUID = 525365674255148921L;
		int x, y;
		int dir = -1;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {

			Position p = (Position) obj;
			if (p.x != this.x)
				return false;
			if (p.y != this.y)
				return false;
			return true;

		}

		public int getDir() {
			return dir;
		}

		public void setDir(int dir) {
			this.dir = dir > 8 ? dir - 8 : dir;
		}

		@Override
		public String toString() {
			return "Position [x=" + x + ", y=" + y + ", dir=" + dir + "]";
		}

	}
}
