;; --------------------------------------------------------------------------------------
;; Basic ARIES bindings
;; --------------------------------------------------------------------------------------

(ns aries
  (:use [clj-span.aries-span-bridge :only (span-driver)]))
(refer 'tl          :only '(listp))
(refer 'corescience :only '(get-observable-class))
(refer 'modelling :only '(transform-model))

(defn j-make-span
	"Make a new instance of a GSSM model and return it. Should be private, but must be public to work 
	within the gssm macro. We need a compiled proxy because the Java classes aren't visible at runtime."
	[]
	(new org.integratedmodelling.aries.core.models.SPANModel))
	
(defn get-span-proxy
	"Create a Java object to handle a SPAN run."
	[]
	(proxy [org.integratedmodelling.aries.core.span.SPANProxy] []
		(runSPAN [observation source-concept use-concept sink-concept flow-concept flow-params] 
			(span-driver observation source-concept use-concept sink-concept flow-concept flow-params))))
			
;; a static object will suffice, this is thread-safe to the point of boredom
(org.integratedmodelling.aries.core.implementations.observations.SPANTransformer/setSPANProxy (get-span-proxy))

(defmacro span
	"Create a SPAN model. The observable must be a flow concept. This one admits specification of all 
	SPAN flow parameters inside the span form. The context models will be mapped to the source, use, 
	and sink observables; any other dependents whose observable is not a source, sink or use type will
	be dependencies for the flow model."
	[observable source-obs use-obs sink-obs flow-obs flow-data-obs & body]
	`(let [model# (j-make-span)] 
 	   (.setObservable model# (if (seq? ~observable) (listp ~observable) ~observable))
 	   (.setFlowObservables model# (tl/conc ~source-obs) (tl/conc ~use-obs) (tl/conc ~sink-obs) (tl/conc ~flow-obs) (tl/conc ~flow-data-obs))
 	   (if (not (nil? '~body)) 
				(doseq [classifier# (partition 2 '~body)]
		 	   	(if  (keyword? (first classifier#)) 
		 	   		  (transform-model model# classifier#))))
 	   model#))
			
