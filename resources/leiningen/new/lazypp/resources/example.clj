(defmethod parse :big [[_ options content more]]
  (concat
   (create-attributes options)
   (create-nodes options)
   (parse content)
   (when (not (nil? more))
     (parse more))))
