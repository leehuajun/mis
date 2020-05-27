package com.sunjet.mis.module.report.repository;

        import com.sunjet.mis.module.report.view.PredictingDemandRefittedVehiclesView;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
/**
 * @author SUNJET-YFS
 * @Title: PredictingDemandRefittedVehiclesRepository
 * @ProjectName mis
 * @Description: 货改车月预测改装车销售需求计划
 * @date 2019/4/2423:40
 */
public interface PredictingDemandRefittedVehiclesRepository extends JpaRepository<PredictingDemandRefittedVehiclesView, String>, JpaSpecificationExecutor<PredictingDemandRefittedVehiclesView> {

}
