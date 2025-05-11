package id.bmp.miner.repository;

import id.bmp.miner.model.ScalpingSignal;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScalpingSignalRepository extends BaseRepository {

    public ScalpingSignalRepository() {
        log = getLogger(this.getClass());
    }

    public List<ScalpingSignal> findScalpingInOpenPosition() {
        String methodName = "findScalpingInOpenPosition";
        start(methodName);
        List<ScalpingSignal> scalpingSignals = new ArrayList<>();

        String sql = "SELECT id, market, signal_time, buy_price, buy_amount, initial_capital, sell_price, profit, ema_fast, " +
                "ema_slow, rsi, body_ratio, volume, avg_volume, is_body_strong AS bodyStrong, is_volume_spike AS volumeSpike, signal_type, is_executed AS executed, " +
                "is_open_position AS openPosition, is_pump AS pompom " +
                "FROM scalping_signal " +
                "WHERE is_open_position = true";
        try (Handle h = getHandle(); Query query = h.createQuery(sql)) {
            scalpingSignals = query.mapToBean(ScalpingSignal.class).list();
        } catch (Exception ex) {
            log.error(methodName, ex);
        }

        completed(methodName);
        return scalpingSignals;
    }

    public boolean insertScalpingSignal(ScalpingSignal scalpingSignal) {
        String methodName = "insertScalpingSignal";
        start(methodName);
        boolean result = false;

        String sql = "INSERT INTO crypto_4life.scalping_signal (" +
                "    market, signal_time, buy_price, buy_amount, initial_capital," +
                "    sell_price, profit, ema_fast, ema_slow, rsi, body_ratio," +
                "    volume, avg_volume, is_body_strong, is_volume_spike," +
                "    signal_type, is_executed, is_open_position, is_pump" +
                ") VALUES (" +
                "    :market, :signalTime, :buyPrice, :buyAmount, :initialCapital," +
                "    :sellPrice, :profit, :emaFast, :emaSlow, :rsi, :bodyRatio," +
                "    :volume, :avgVolume, :bodyStrong, :volumeSpike," +
                "    :signalType, :executed, :openPosition, :pompom" +
                ")";

        try (Handle h = getHandle(); Update update = h.createUpdate(sql)) {
            update.bindBean(scalpingSignal);
            result = executeUpdate(update);
            log.debug(methodName, "Result: " + result);
        } catch (Exception ex) {
            log.error(methodName, ex);
        }
        completed(methodName);
        return result;
    }
}
