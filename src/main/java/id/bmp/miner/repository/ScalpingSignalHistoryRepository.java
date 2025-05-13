package id.bmp.miner.repository;

import id.bmp.miner.model.ScalpingSignal;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ScalpingSignalHistoryRepository extends BaseRepository {

    public ScalpingSignalHistoryRepository() {
        log = getLogger(this.getClass());
    }

    public boolean insertScalpingSignal(ScalpingSignal scalpingSignal) {
        String methodName = "insertScalpingSignal";
        start(methodName);
        boolean result = false;

        String sql = "INSERT INTO scalping_signal_history (" +
                "    market, transaction_id, signal_time, buy_price, buy_amount, initial_capital," +
                "    sell_price, profit, ema_fast, ema_slow, rsi, body_ratio," +
                "    volume, avg_volume, is_body_strong, is_volume_spike," +
                "    signal_type, is_executed, is_open_position, is_pump" +
                ") VALUES (" +
                "    :market, transactionId, :signalTime, :buyPrice, :buyAmount, :initialCapital," +
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
